package dao;

import model.Recipe;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Recipe operations
 */
public class RecipeDAO {
    
    /**
     * Create a new recipe
     * @param recipe Recipe object to create
     * @return Generated recipe ID, or -1 if failed
     */
    public int createRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (user_id, title, ingredients, description, image_path, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, recipe.getUserId());
            stmt.setString(2, recipe.getTitle());
            stmt.setString(3, recipe.getIngredients());
            stmt.setString(4, recipe.getDescription());
            stmt.setString(5, recipe.getImagePath());
            stmt.setString(6, recipe.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating recipe: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get all approved recipes with user information and counts
     * @param currentUserId Current user ID for like status (0 if not logged in)
     * @return List of approved recipes
     */
    public List<Recipe> getApprovedRecipes(int currentUserId) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.recipe_id, r.user_id, r.title, r.ingredients, r.description, " +
                    "r.image_path, r.status, r.created_at, u.name as user_name, " +
                    "COALESCE(like_counts.like_count, 0) as like_count, " +
                    "COALESCE(comment_counts.comment_count, 0) as comment_count, " +
                    "CASE WHEN user_likes.user_id IS NOT NULL THEN 1 ELSE 0 END as liked_by_user " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as like_count FROM likes GROUP BY recipe_id) like_counts " +
                    "ON r.recipe_id = like_counts.recipe_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as comment_count FROM comments GROUP BY recipe_id) comment_counts " +
                    "ON r.recipe_id = comment_counts.recipe_id " +
                    "LEFT JOIN likes user_likes ON r.recipe_id = user_likes.recipe_id AND user_likes.user_id = ? " +
                    "WHERE r.status = 'APPROVED' " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, currentUserId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipeWithCounts(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting approved recipes: " + e.getMessage());
        }
        
        return recipes;
    }
    
    /**
     * Get recipes by user ID
     * @param userId User ID
     * @return List of user's recipes
     */
    public List<Recipe> getRecipesByUserId(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.recipe_id, r.user_id, r.title, r.ingredients, r.description, " +
                    "r.image_path, r.status, r.created_at, u.name as user_name, " +
                    "COALESCE(like_counts.like_count, 0) as like_count, " +
                    "COALESCE(comment_counts.comment_count, 0) as comment_count " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as like_count FROM likes GROUP BY recipe_id) like_counts " +
                    "ON r.recipe_id = like_counts.recipe_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as comment_count FROM comments GROUP BY recipe_id) comment_counts " +
                    "ON r.recipe_id = comment_counts.recipe_id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapResultSetToRecipeWithCounts(rs);
                    recipe.setLikedByCurrentUser(false); // User's own recipes
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting recipes by user ID: " + e.getMessage());
        }
        
        return recipes;
    }
    
    /**
     * Get recipe by ID
     * @param recipeId Recipe ID
     * @param currentUserId Current user ID for like status
     * @return Recipe object or null if not found
     */
    public Recipe getRecipeById(int recipeId, int currentUserId) {
        String sql = "SELECT r.recipe_id, r.user_id, r.title, r.ingredients, r.description, " +
                    "r.image_path, r.status, r.created_at, u.name as user_name, " +
                    "COALESCE(like_counts.like_count, 0) as like_count, " +
                    "COALESCE(comment_counts.comment_count, 0) as comment_count, " +
                    "CASE WHEN user_likes.user_id IS NOT NULL THEN 1 ELSE 0 END as liked_by_user " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as like_count FROM likes GROUP BY recipe_id) like_counts " +
                    "ON r.recipe_id = like_counts.recipe_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as comment_count FROM comments GROUP BY recipe_id) comment_counts " +
                    "ON r.recipe_id = comment_counts.recipe_id " +
                    "LEFT JOIN likes user_likes ON r.recipe_id = user_likes.recipe_id AND user_likes.user_id = ? " +
                    "WHERE r.recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setInt(2, recipeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipeWithCounts(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting recipe by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all recipes for admin (all statuses)
     * @return List of all recipes
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.recipe_id, r.user_id, r.title, r.ingredients, r.description, " +
                    "r.image_path, r.status, r.created_at, u.name as user_name, " +
                    "COALESCE(like_counts.like_count, 0) as like_count, " +
                    "COALESCE(comment_counts.comment_count, 0) as comment_count " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as like_count FROM likes GROUP BY recipe_id) like_counts " +
                    "ON r.recipe_id = like_counts.recipe_id " +
                    "LEFT JOIN (SELECT recipe_id, COUNT(*) as comment_count FROM comments GROUP BY recipe_id) comment_counts " +
                    "ON r.recipe_id = comment_counts.recipe_id " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Recipe recipe = mapResultSetToRecipeWithCounts(rs);
                recipe.setLikedByCurrentUser(false); // Admin view
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all recipes: " + e.getMessage());
        }
        
        return recipes;
    }
    
    /**
     * Get pending recipes for admin approval
     * @return List of pending recipes
     */
    public List<Recipe> getPendingRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.recipe_id, r.user_id, r.title, r.ingredients, r.description, " +
                    "r.image_path, r.status, r.created_at, u.name as user_name " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.status = 'PENDING' " +
                    "ORDER BY r.created_at ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                recipes.add(mapResultSetToRecipe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending recipes: " + e.getMessage());
        }
        
        return recipes;
    }
    
    /**
     * Update recipe status
     * @param recipeId Recipe ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRecipeStatus(int recipeId, String status) {
        String sql = "UPDATE recipes SET status = ? WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, recipeId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating recipe status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete recipe by ID
     * @param recipeId Recipe ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRecipe(int recipeId) {
        String sql = "DELETE FROM recipes WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recipeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting recipe: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get total recipe count
     * @return Total number of recipes
     */
    public int getTotalRecipeCount() {
        String sql = "SELECT COUNT(*) FROM recipes";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total recipe count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get pending recipe count
     * @return Number of pending recipes
     */
    public int getPendingRecipeCount() {
        String sql = "SELECT COUNT(*) FROM recipes WHERE status = 'PENDING'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending recipe count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Recipe object
     * @param rs ResultSet from database query
     * @return Recipe object
     * @throws SQLException if mapping fails
     */
    private Recipe mapResultSetToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setUserId(rs.getInt("user_id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImagePath(rs.getString("image_path"));
        recipe.setStatus(rs.getString("status"));
        recipe.setCreatedAt(rs.getTimestamp("created_at"));
        recipe.setUserName(rs.getString("user_name"));
        return recipe;
    }
    
    /**
     * Map ResultSet to Recipe object with counts
     * @param rs ResultSet from database query
     * @return Recipe object with like and comment counts
     * @throws SQLException if mapping fails
     */
    private Recipe mapResultSetToRecipeWithCounts(ResultSet rs) throws SQLException {
        Recipe recipe = mapResultSetToRecipe(rs);
        recipe.setLikeCount(rs.getInt("like_count"));
        recipe.setCommentCount(rs.getInt("comment_count"));
        recipe.setLikedByCurrentUser(rs.getBoolean("liked_by_user"));
        return recipe;
    }
}