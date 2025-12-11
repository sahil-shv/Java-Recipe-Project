package service;

import dao.RecipeDAO;
import dao.RatingDAO;
import model.Recipe;
import model.Rating;
import utils.Logger;
import utils.Pagination;

import java.util.List;

public class RecipeService {
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final RatingDAO ratingDAO = new RatingDAO();

    public boolean addRecipe(Recipe recipe) {
        if (recipe.getTitle() == null || recipe.getTitle().trim().isEmpty()) {
            Logger.warning("Recipe addition failed: Title is required");
            return false;
        }
        if (recipe.getIngredients() == null || recipe.getIngredients().trim().isEmpty()) {
            Logger.warning("Recipe addition failed: Ingredients are required");
            return false;
        }
        if (recipe.getSteps() == null || recipe.getSteps().trim().isEmpty()) {
            Logger.warning("Recipe addition failed: Steps are required");
            return false;
        }

        return recipeDAO.addRecipe(recipe);
    }

    public Recipe getRecipeById(int id) {
        return recipeDAO.getRecipeById(id);
    }

    public Pagination<Recipe> getRecipesPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Recipe> recipes = recipeDAO.getRecipesWithPagination(pageSize, offset);
        int totalItems = recipeDAO.getTotalRecipeCount();
        return new Pagination<>(recipes, page, pageSize, totalItems);
    }

    public Pagination<Recipe> searchRecipes(String searchTerm, int page, int pageSize) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getRecipesPaginated(page, pageSize);
        }

        int offset = (page - 1) * pageSize;
        List<Recipe> recipes = recipeDAO.searchRecipes(searchTerm.trim(), pageSize, offset);
        int totalItems = recipeDAO.getSearchCount(searchTerm.trim());
        return new Pagination<>(recipes, page, pageSize, totalItems);
    }

    public List<Recipe> getRecipesByUserId(int userId) {
        return recipeDAO.getRecipesByUserId(userId);
    }

    public boolean addOrUpdateRating(int userId, int recipeId, int rating) {
        if (rating < 1 || rating > 5) {
            Logger.warning("Rating failed: Rating must be between 1 and 5");
            return false;
        }

        Rating ratingObj = new Rating(recipeId, userId, rating);
        return ratingDAO.addOrUpdateRating(ratingObj);
    }

    public Rating getUserRating(int userId, int recipeId) {
        return ratingDAO.getRatingByUserAndRecipe(userId, recipeId);
    }
}

