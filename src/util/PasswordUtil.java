package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {
    private static final SecureRandom random = new SecureRandom();
    
    private PasswordUtil() {
    }

    /**
     * Generate a random salt for password hashing
     */
    public static String generateSalt() {
        byte[] saltBytes = new byte[32];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * Hash password with SHA-256 and salt
     */
    public static String hashPassword(String password, String salt) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (salt == null) {
            throw new IllegalArgumentException("Salt cannot be null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }

    /**
     * Verify password against stored hash and salt
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) {
        if (password == null || salt == null || storedHash == null) {
            return false;
        }
        String computedHash = hashPassword(password, salt);
        return computedHash.equals(storedHash);
    }
}
