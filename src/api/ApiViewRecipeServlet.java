package api;

import dao.CommentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.Recipe;
import model.Rating;
import model.User;
import service.RecipeService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/recipes/*")
public class ApiViewRecipeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RecipeService recipeService = new RecipeService();
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Recipe ID required");
            return;
        }

        try {
            int recipeId = Integer.parseInt(pathInfo.substring(1));
            Recipe recipe = recipeService.getRecipeById(recipeId);
            
            if (recipe == null) {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Recipe not found");
                return;
            }

            List<Comment> comments = commentDAO.getCommentsByRecipeId(recipeId);
            
            HttpSession session = req.getSession(false);
            Rating userRating = null;
            User currentUser = null;
            
            if (session != null) {
                Object userObj = session.getAttribute("currentUser");
                if (userObj instanceof User) {
                    currentUser = (User) userObj;
                    userRating = recipeService.getUserRating(currentUser.getId(), recipeId);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("recipe", recipe);
            response.put("comments", comments);
            response.put("userRating", userRating);
            response.put("currentUser", currentUser != null ? Map.of(
                "id", currentUser.getId(),
                "name", currentUser.getName(),
                "email", currentUser.getEmail()
            ) : null);

            JsonUtil.sendJsonResponse(resp, response);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe ID");
        } catch (Exception e) {
            utils.Logger.error("Error fetching recipe", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching recipe");
        }
    }
}

