package controller;

import dao.LikeDAO;
import model.Like;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling recipe likes/unlikes
 */
@WebServlet("/user/like-recipe")
public class LikeRecipeServlet extends HttpServlet {
    
    private LikeDAO likeDAO;
    
    @Override
    public void init() throws ServletException {
        likeDAO = new LikeDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String recipeIdParam = request.getParameter("recipeId");
        String action = request.getParameter("action"); // "like" or "unlike"
        
        if (recipeIdParam == null || action == null) {
            response.sendRedirect("dashboard?error=Invalid request");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            
            if ("like".equals(action)) {
                // Add like
                if (!likeDAO.hasUserLikedRecipe(user.getUserId(), recipeId)) {
                    Like like = new Like(user.getUserId(), recipeId);
                    likeDAO.addLike(like);
                }
            } else if ("unlike".equals(action)) {
                // Remove like
                likeDAO.removeLike(user.getUserId(), recipeId);
            }
            
            // Redirect back to the referring page
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect("dashboard");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=Invalid recipe ID");
        } catch (Exception e) {
            System.err.println("Error handling like/unlike: " + e.getMessage());
            response.sendRedirect("dashboard?error=Error processing request");
        }
    }
}