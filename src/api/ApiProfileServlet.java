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
import service.UserService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/profile")
public class ApiProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();
    private final RecipeService recipeService = new RecipeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        
        if (currentUser == null) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        try {
            User fullUser = userService.getUserById(currentUser.getId());
            if (fullUser == null) {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            List<Recipe> userRecipes = recipeService.getRecipesByUserId(fullUser.getId());
            int recipeCount = userService.getRecipeCount(fullUser.getId());
            double avgRating = userService.getAverageRatingOfRecipes(fullUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("user", fullUser);
            response.put("recipes", userRecipes);
            response.put("recipeCount", recipeCount);
            response.put("avgRating", avgRating);

            JsonUtil.sendJsonResponse(resp, response);
        } catch (Exception e) {
            utils.Logger.error("Error fetching profile", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching profile");
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

