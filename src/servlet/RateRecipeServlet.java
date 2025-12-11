package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.RecipeService;

import java.io.IOException;

public class RateRecipeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RecipeService recipeService = new RecipeService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(withContext(req, "/login.html"));
            return;
        }

        Object userObj = session.getAttribute("currentUser");
        if (!(userObj instanceof User)) {
            resp.sendRedirect(withContext(req, "/login.html"));
            return;
        }

        User currentUser = (User) userObj;
        String recipeIdParam = req.getParameter("recipeId");
        String ratingParam = req.getParameter("rating");

        if (recipeIdParam == null || ratingParam == null) {
            resp.sendRedirect(withContext(req, "/recipes.html"));
            return;
        }

        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            int rating = Integer.parseInt(ratingParam);

            if (rating < 1 || rating > 5) {
                resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId + "&error=invalidRating"));
                return;
            }

            boolean success = recipeService.addOrUpdateRating(currentUser.getId(), recipeId, rating);
            if (success) {
                resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId + "&rated=true"));
            } else {
                resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId + "&error=ratingFailed"));
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(withContext(req, "/recipes.html"));
        }
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}

