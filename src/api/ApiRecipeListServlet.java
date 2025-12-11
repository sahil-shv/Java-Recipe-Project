package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Recipe;
import model.User;
import service.RecipeService;
import utils.JsonUtil;
import utils.Pagination;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/recipes")
public class ApiRecipeListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RecipeService recipeService = new RecipeService();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String myRecipesParam = req.getParameter("myRecipes");
        String pageParam = req.getParameter("page");
        int page = 1;

        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        Pagination<Recipe> pagination;
        List<Recipe> recipes;
        boolean isMyRecipes = false;
        
        try {
            if ("true".equalsIgnoreCase(myRecipesParam)) {
                HttpSession session = req.getSession(false);
                User currentUser = getAuthenticatedUser(session);
                if (currentUser == null) {
                    JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
                    return;
                }
                
                recipes = recipeService.getRecipesByUserId(currentUser.getId());
                pagination = new Pagination<>(recipes, 1, recipes.size(), recipes.size());
                isMyRecipes = true;
            } else if (search != null && !search.trim().isEmpty()) {
                pagination = recipeService.searchRecipes(search, page, PAGE_SIZE);
            } else {
                pagination = recipeService.getRecipesPaginated(page, PAGE_SIZE);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("recipes", pagination.getItems());
            response.put("pagination", Map.of(
                "currentPage", pagination.getCurrentPage(),
                "pageSize", pagination.getPageSize(),
                "totalItems", pagination.getTotalItems(),
                "totalPages", pagination.getTotalPages(),
                "hasNext", pagination.hasNext(),
                "hasPrevious", pagination.hasPrevious()
            ));
            response.put("searchTerm", search);
            response.put("myRecipes", isMyRecipes);

            JsonUtil.sendJsonResponse(resp, response);
        } catch (Exception e) {
            utils.Logger.error("Error fetching recipes", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching recipes");
        }
    }

    private User getAuthenticatedUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userObj = session.getAttribute("currentUser");
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;
    }
}

