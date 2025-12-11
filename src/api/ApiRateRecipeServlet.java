package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.RecipeService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/rate")
public class ApiRateRecipeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RecipeService recipeService = new RecipeService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        
        if (currentUser == null) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        String recipeIdParam = req.getParameter("recipeId");
        String ratingParam = req.getParameter("rating");

        if (recipeIdParam == null || ratingParam == null) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Recipe ID and rating required");
            return;
        }

        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            int rating = Integer.parseInt(ratingParam);

            if (rating < 1 || rating > 5) {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Rating must be between 1 and 5");
                return;
            }

            boolean success = recipeService.addOrUpdateRating(currentUser.getId(), recipeId, rating);
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Rating saved successfully");
                JsonUtil.sendJsonResponse(resp, response);
            } else {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save rating");
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe ID or rating");
        } catch (Exception e) {
            utils.Logger.error("Error rating recipe", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error rating recipe");
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

