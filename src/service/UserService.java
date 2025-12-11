package service;

import dao.EmailVerificationTokenDAO;
import dao.UserDAO;
import model.EmailVerificationToken;
import model.User;
import util.PasswordUtil;
import utils.Logger;
import utils.MailUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final EmailVerificationTokenDAO tokenDAO = new EmailVerificationTokenDAO();
    private static final SecureRandom random = new SecureRandom();

    public boolean registerUser(User user, String baseUrl) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            Logger.warning("Registration failed: Name is required");
            return false;
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            Logger.warning("Registration failed: Email is required");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            Logger.warning("Registration failed: Password must be at least 6 characters");
            return false;
        }

        if (userDAO.isEmailTaken(user.getEmail())) {
            Logger.warning("Registration failed: Email already taken: " + user.getEmail());
            return false;
        }

        boolean registered = userDAO.registerUser(user);
        if (registered) {
            User savedUser = userDAO.authenticate(user.getEmail(), user.getPassword());
            if (savedUser != null) {
                sendVerificationEmail(savedUser, baseUrl);
            }
        }
        return registered;
    }

    public User authenticate(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null) {
            return null;
        }
        return userDAO.authenticate(email.trim(), password);
    }

    public boolean isEmailVerified(User user) {
        return user != null && user.isVerified();
    }

    private void sendVerificationEmail(User user, String baseUrl) {
        String token = generateVerificationToken();
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);
        
        EmailVerificationToken verificationToken = new EmailVerificationToken(user.getId(), token, expiry);
        if (tokenDAO.saveToken(verificationToken)) {
            String verificationLink = baseUrl + "/verifyEmail?token=" + token;
            MailUtil.sendVerificationEmail(user.getEmail(), user.getName(), verificationLink);
        }
    }

    private String generateVerificationToken() {
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public boolean verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenDAO.getTokenByTokenString(token);
        if (verificationToken == null) {
            Logger.warning("Verification failed: Token not found");
            return false;
        }

        if (verificationToken.getExpiry().isBefore(LocalDateTime.now())) {
            Logger.warning("Verification failed: Token expired");
            tokenDAO.deleteToken(token);
            return false;
        }

        boolean verified = userDAO.verifyUser(verificationToken.getUserId());
        if (verified) {
            tokenDAO.deleteToken(token);
            Logger.info("Email verified for user: " + verificationToken.getUserId());
        }
        return verified;
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public boolean updateProfileImage(int userId, String imagePath) {
        return userDAO.updateProfileImage(userId, imagePath);
    }

    public boolean updatePassword(int userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            Logger.warning("Password update failed: New password must be at least 6 characters");
            return false;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }

        // Verify old password - need to get from DB with salt
        User authenticated = userDAO.authenticate(user.getEmail(), oldPassword);
        if (authenticated == null) {
            Logger.warning("Password update failed: Old password incorrect");
            return false;
        }

        return userDAO.updatePassword(userId, newPassword);
    }

    public int getRecipeCount(int userId) {
        return userDAO.getRecipeCountByUserId(userId);
    }

    public double getAverageRatingOfRecipes(int userId) {
        return userDAO.getAverageRatingOfUserRecipes(userId);
    }
}

