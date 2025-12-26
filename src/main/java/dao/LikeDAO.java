package dao;

import model.Like;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Like operations
 */
public class LikeDAO {
    
    /**
     * Add a like to a recipe
     * @param like Like object to create
     * @return Generated like ID, or -1 if failed
     */
    public int addLike(Like like) {
        String sql = "INSERT INTO likes (user_id, recipe_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, like.getUserId());
            stmt.setInt(2, like.getRecipeId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding like: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Remove a like from a recipe
     * @param userId User ID
     * @param recipeId Recipe ID
     * @return true if successful, false otherwise
     */
    public boolean removeLike(int userId, int recipeId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing like: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if user has liked a recipe
     * @param userId User ID
     * @param recipeId Recipe ID
     * @return true if liked, false otherwise
     */
    public boolean hasUserLikedRecipe(int userId, int recipeId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user liked recipe: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get like count for a recipe
     * @param recipeId Recipe ID
     * @return Number of likes
     */
    public int getLikeCount(int recipeId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting like count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get all likes for a recipe
     * @param recipeId Recipe ID
     * @return List of likes
     */
    public List<Like> getLikesByRecipeId(int recipeId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT like_id, user_id, recipe_id, created_at FROM likes WHERE recipe_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likes.add(mapResultSetToLike(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting likes by recipe ID: " + e.getMessage());
        }
        
        return likes;
    }
    
    /**
     * Get all likes by a user
     * @param userId User ID
     * @return List of likes
     */
    public List<Like> getLikesByUserId(int userId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT like_id, user_id, recipe_id, created_at FROM likes WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likes.add(mapResultSetToLike(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting likes by user ID: " + e.getMessage());
        }
        
        return likes;
    }
    
    /**
     * Get total like count across all recipes
     * @return Total number of likes
     */
    public int getTotalLikeCount() {
        String sql = "SELECT COUNT(*) FROM likes";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total like count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Delete all likes for a recipe (used when deleting a recipe)
     * @param recipeId Recipe ID
     * @return true if successful, false otherwise
     */
    public boolean deleteLikesByRecipeId(int recipeId) {
        String sql = "DELETE FROM likes WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            return stmt.executeUpdate() >= 0; // Can be 0 if no likes exist
        } catch (SQLException e) {
            System.err.println("Error deleting likes by recipe ID: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Map ResultSet to Like object
     * @param rs ResultSet from database query
     * @return Like object
     * @throws SQLException if mapping fails
     */
    private Like mapResultSetToLike(ResultSet rs) throws SQLException {
        Like like = new Like();
        like.setLikeId(rs.getInt("like_id"));
        like.setUserId(rs.getInt("user_id"));
        like.setRecipeId(rs.getInt("recipe_id"));
        like.setCreatedAt(rs.getTimestamp("created_at"));
        return like;
    }
}