package dao;

import model.Recipe;
import model.User;
import util.DBConnection;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private final RatingDAO ratingDAO = new RatingDAO();
    
    public boolean addRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (title, ingredients, steps, category, image, user_id, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getTitle());
            stmt.setString(2, recipe.getIngredients());
            stmt.setString(3, recipe.getSteps());
            stmt.setString(4, recipe.getCategory());
            stmt.setString(5, recipe.getImage());
            stmt.setInt(6, recipe.getUserId());
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Recipe added: " + recipe.getTitle() + " by user " + recipe.getUserId());
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error adding recipe", e);
            return false;
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.title, r.ingredients, r.steps, r.category, r.image, r.user_id, r.created_at, u.name, u.email FROM recipes r JOIN users u ON r.user_id = u.id ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Recipe recipe = mapRecipe(rs);
                recipe.setAverageRating(ratingDAO.getAverageRating(recipe.getId()));
                recipe.setTotalRatings(ratingDAO.getTotalRatings(recipe.getId()));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            Logger.error("Error getting all recipes", e);
        }
        return recipes;
    }

    public List<Recipe> searchRecipes(String searchTerm, int limit, int offset) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.title, r.ingredients, r.steps, r.category, r.image, r.user_id, r.created_at, u.name, u.email " +
                     "FROM recipes r JOIN users u ON r.user_id = u.id " +
                     "WHERE r.title LIKE ? OR r.ingredients LIKE ? OR r.category LIKE ? " +
                     "ORDER BY r.created_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setInt(4, limit);
            stmt.setInt(5, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapRecipe(rs);
                    recipe.setAverageRating(ratingDAO.getAverageRating(recipe.getId()));
                    recipe.setTotalRatings(ratingDAO.getTotalRatings(recipe.getId()));
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error searching recipes with term: " + searchTerm, e);
        }
        return recipes;
    }

    public int getSearchCount(String searchTerm) {
        String sql = "SELECT COUNT(*) as total FROM recipes r " +
                     "WHERE r.title LIKE ? OR r.ingredients LIKE ? OR r.category LIKE ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting search count for term: " + searchTerm, e);
        }
        return 0;
    }

    public List<Recipe> getRecipesWithPagination(int limit, int offset) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.title, r.ingredients, r.steps, r.category, r.image, r.user_id, r.created_at, u.name, u.email " +
                     "FROM recipes r JOIN users u ON r.user_id = u.id ORDER BY r.created_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapRecipe(rs);
                    recipe.setAverageRating(ratingDAO.getAverageRating(recipe.getId()));
                    recipe.setTotalRatings(ratingDAO.getTotalRatings(recipe.getId()));
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting recipes with pagination", e);
        }
        return recipes;
    }

    public int getTotalRecipeCount() {
        String sql = "SELECT COUNT(*) as total FROM recipes";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            Logger.error("Error getting total recipe count", e);
        }
        return 0;
    }

    public List<Recipe> getRecipesByUserId(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id, r.title, r.ingredients, r.steps, r.category, r.image, r.user_id, r.created_at, u.name, u.email " +
                     "FROM recipes r JOIN users u ON r.user_id = u.id WHERE r.user_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapRecipe(rs);
                    recipe.setAverageRating(ratingDAO.getAverageRating(recipe.getId()));
                    recipe.setTotalRatings(ratingDAO.getTotalRatings(recipe.getId()));
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting recipes by user ID: " + userId, e);
        }
        return recipes;
    }

    public Recipe getRecipeById(int id) {
        String sql = "SELECT r.id, r.title, r.ingredients, r.steps, r.category, r.image, r.user_id, r.created_at, u.name, u.email FROM recipes r JOIN users u ON r.user_id = u.id WHERE r.id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Recipe recipe = mapRecipe(rs);
                    recipe.setAverageRating(ratingDAO.getAverageRating(recipe.getId()));
                    recipe.setTotalRatings(ratingDAO.getTotalRatings(recipe.getId()));
                    return recipe;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting recipe by ID: " + id, e);
        }
        return null;
    }

    private Recipe mapRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setSteps(rs.getString("steps"));
        recipe.setCategory(rs.getString("category"));
        recipe.setImage(rs.getString("image"));
        recipe.setUserId(rs.getInt("user_id"));
        if (rs.getTimestamp("created_at") != null) {
            recipe.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        User author = new User();
        author.setId(rs.getInt("user_id"));
        author.setName(rs.getString("name"));
        author.setEmail(rs.getString("email"));
        recipe.setAuthor(author);
        return recipe;
    }
}
