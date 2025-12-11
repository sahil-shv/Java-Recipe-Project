package dao;

import model.EmailVerificationToken;
import util.DBConnection;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EmailVerificationTokenDAO {
    
    public boolean saveToken(EmailVerificationToken token) {
        String sql = "INSERT INTO email_verification_tokens (user_id, token, expiry) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getToken());
            stmt.setTimestamp(3, Timestamp.valueOf(token.getExpiry()));
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Verification token saved for user " + token.getUserId());
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error saving verification token", e);
            return false;
        }
    }

    public EmailVerificationToken getTokenByTokenString(String tokenString) {
        String sql = "SELECT id, user_id, token, expiry, created_at FROM email_verification_tokens WHERE token = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenString);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EmailVerificationToken token = new EmailVerificationToken();
                    token.setId(rs.getInt("id"));
                    token.setUserId(rs.getInt("user_id"));
                    token.setToken(rs.getString("token"));
                    if (rs.getTimestamp("expiry") != null) {
                        token.setExpiry(rs.getTimestamp("expiry").toLocalDateTime());
                    }
                    if (rs.getTimestamp("created_at") != null) {
                        token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    return token;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting token by token string", e);
        }
        return null;
    }

    public boolean deleteToken(String tokenString) {
        String sql = "DELETE FROM email_verification_tokens WHERE token = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenString);
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Verification token deleted: " + tokenString);
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error deleting verification token", e);
            return false;
        }
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM email_verification_tokens WHERE expiry < NOW()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                Logger.info("Deleted " + deleted + " expired verification tokens");
            }
        } catch (SQLException e) {
            Logger.error("Error deleting expired tokens", e);
        }
    }
}

