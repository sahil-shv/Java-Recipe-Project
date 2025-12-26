package dao;

import model.Comment;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Comment operations
 */
public class CommentDAO {
    
    /**
     * Add a comment to a recipe
     * @param comment Comment object to create
     * @return Generated comment ID, or -1 if failed
     */
    public int addComment(Comment comment) {
        String sql = "INSERT INTO comments (user_id, recipe_id, comment) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, comment.getUserId());
            stmt.setInt(2, comment.getRecipeId());
            stmt.setString(3, comment.getComment());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding comment: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get all comments for a recipe
     * @param recipeId Recipe ID
     * @return List of comments with user names
     */
    public List<Comment> getCommentsByRecipeId(int recipeId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.comment_id, c.user_id, c.recipe_id, c.comment, c.created_at, u.name as user_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.recipe_id = ? " +
                    "ORDER BY c.created_at ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting comments by recipe ID: " + e.getMessage());
        }
        
        return comments;
    }
    
    /**
     * Get all comments by a user
     * @param userId User ID
     * @return List of comments
     */
    public List<Comment> getCommentsByUserId(int userId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.comment_id, c.user_id, c.recipe_id, c.comment, c.created_at, u.name as user_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.user_id = ? " +
                    "ORDER BY c.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting comments by user ID: " + e.getMessage());
        }
        
        return comments;
    }
    
    /**
     * Get all comments (for admin)
     * @return List of all comments with user and recipe information
     */
    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.comment_id, c.user_id, c.recipe_id, c.comment, c.created_at, " +
                    "u.name as user_name, r.title as recipe_title " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "JOIN recipes r ON c.recipe_id = r.recipe_id " +
                    "ORDER BY c.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Comment comment = mapResultSetToComment(rs);
                // You can add recipe title to comment if needed
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all comments: " + e.getMessage());
        }
        
        return comments;
    }
    
    /**
     * Get comment by ID
     * @param commentId Comment ID
     * @return Comment object or null if not found
     */
    public Comment getCommentById(int commentId) {
        String sql = "SELECT c.comment_id, c.user_id, c.recipe_id, c.comment, c.created_at, u.name as user_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.comment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToComment(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting comment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update comment content
     * @param commentId Comment ID
     * @param newComment New comment content
     * @return true if successful, false otherwise
     */
    public boolean updateComment(int commentId, String newComment) {
        String sql = "UPDATE comments SET comment = ? WHERE comment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newComment);
            stmt.setInt(2, commentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating comment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete comment by ID
     * @param commentId Comment ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting comment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get comment count for a recipe
     * @param recipeId Recipe ID
     * @return Number of comments
     */
    public int getCommentCount(int recipeId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting comment count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get total comment count across all recipes
     * @return Total number of comments
     */
    public int getTotalCommentCount() {
        String sql = "SELECT COUNT(*) FROM comments";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total comment count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Delete all comments for a recipe (used when deleting a recipe)
     * @param recipeId Recipe ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCommentsByRecipeId(int recipeId) {
        String sql = "DELETE FROM comments WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            return stmt.executeUpdate() >= 0; // Can be 0 if no comments exist
        } catch (SQLException e) {
            System.err.println("Error deleting comments by recipe ID: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Map ResultSet to Comment object
     * @param rs ResultSet from database query
     * @return Comment object
     * @throws SQLException if mapping fails
     */
    private Comment mapResultSetToComment(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setRecipeId(rs.getInt("recipe_id"));
        comment.setComment(rs.getString("comment"));
        comment.setCreatedAt(rs.getTimestamp("created_at"));
        comment.setUserName(rs.getString("user_name"));
        return comment;
    }
}