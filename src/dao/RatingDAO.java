package dao;

import model.Rating;
import util.DBConnection;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RatingDAO {
    
    public boolean addOrUpdateRating(Rating rating) {
        String sql = "INSERT INTO ratings (recipe_id, user_id, rating) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE rating = ?, created_at = CURRENT_TIMESTAMP";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rating.getRecipeId());
            stmt.setInt(2, rating.getUserId());
            stmt.setInt(3, rating.getRating());
            stmt.setInt(4, rating.getRating());
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Rating added/updated for recipe " + rating.getRecipeId() + " by user " + rating.getUserId());
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error adding/updating rating", e);
            return false;
        }
    }

    public Rating getRatingByUserAndRecipe(int userId, int recipeId) {
        String sql = "SELECT rating_id, recipe_id, user_id, rating, created_at FROM ratings WHERE user_id = ? AND recipe_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rating rating = new Rating();
                    rating.setRatingId(rs.getInt("rating_id"));
                    rating.setRecipeId(rs.getInt("recipe_id"));
                    rating.setUserId(rs.getInt("user_id"));
                    rating.setRating(rs.getInt("rating"));
                    if (rs.getTimestamp("created_at") != null) {
                        rating.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    return rating;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting rating by user and recipe", e);
        }
        return null;
    }

    public double getAverageRating(int recipeId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM ratings WHERE recipe_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting average rating for recipe " + recipeId, e);
        }
        return 0.0;
    }

    public int getTotalRatings(int recipeId) {
        String sql = "SELECT COUNT(*) as total FROM ratings WHERE recipe_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting total ratings for recipe " + recipeId, e);
        }
        return 0;
    }
}

