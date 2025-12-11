package dao;

import model.User;
import util.DBConnection;
import util.PasswordUtil;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password, salt) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("User registered: " + user.getEmail());
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error registering user", e);
            return false;
        }
    }

    public boolean isEmailTaken(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Logger.error("Error checking if email is taken: " + email, e);
            return true;
        }
    }

    public User authenticate(String email, String password) {
        String sql = "SELECT id, name, email, password, salt, is_verified, profile_image FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");
                    if (PasswordUtil.verifyPassword(password, salt, storedHash)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setVerified(rs.getBoolean("is_verified"));
                        user.setProfileImage(rs.getString("profile_image"));
                        Logger.info("User authenticated: " + email);
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error authenticating user: " + email, e);
        }
        return null;
    }

    public User getUserById(int id) {
        String sql = "SELECT id, name, email, is_verified, profile_image FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setVerified(rs.getBoolean("is_verified"));
                    user.setProfileImage(rs.getString("profile_image"));
                    return user;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting user by ID: " + id, e);
        }
        return null;
    }

    public boolean verifyUser(int userId) {
        String sql = "UPDATE users SET is_verified = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("User verified: " + userId);
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error verifying user: " + userId, e);
            return false;
        }
    }

    public boolean updateProfileImage(int userId, String imagePath) {
        String sql = "UPDATE users SET profile_image = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imagePath);
            stmt.setInt(2, userId);
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Profile image updated for user: " + userId);
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error updating profile image for user: " + userId, e);
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ?, salt = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(newPassword, salt);
            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setInt(3, userId);
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Password updated for user: " + userId);
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error updating password for user: " + userId, e);
            return false;
        }
    }

    public int getRecipeCountByUserId(int userId) {
        String sql = "SELECT COUNT(*) as total FROM recipes WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting recipe count for user: " + userId, e);
        }
        return 0;
    }

    public double getAverageRatingOfUserRecipes(int userId) {
        String sql = "SELECT AVG(rt.rating) as avg_rating FROM ratings rt " +
                     "JOIN recipes r ON rt.recipe_id = r.id WHERE r.user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting average rating for user recipes: " + userId, e);
        }
        return 0.0;
    }
}
