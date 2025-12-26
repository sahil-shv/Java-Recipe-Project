package controller;

import dao.CommentDAO;
import dao.RecipeDAO;
import model.Comment;
import model.Recipe;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying recipe details
 */
@WebServlet("/user/recipe-detail")
public class RecipeDetailServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    private CommentDAO commentDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
        commentDAO = new CommentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String recipeIdParam = request.getParameter("id");
        
        if (recipeIdParam == null || recipeIdParam.trim().isEmpty()) {
            response.sendRedirect("dashboard?error=Recipe not found");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            
            // Get recipe details
            Recipe recipe = recipeDAO.getRecipeById(recipeId, user.getUserId());
            
            if (recipe == null) {
                response.sendRedirect("dashboard?error=Recipe not found");
                return;
            }
            
            // Check if recipe is approved (unless it's user's own recipe)
            if (!recipe.isApproved() && recipe.getUserId() != user.getUserId()) {
                response.sendRedirect("dashboard?error=Recipe not available");
                return;
            }
            
            // Get comments for this recipe
            List<Comment> comments = commentDAO.getCommentsByRecipeId(recipeId);
            
            // Set attributes for JSP
            request.setAttribute("recipe", recipe);
            request.setAttribute("comments", comments);
            request.setAttribute("user", user);
            
            // Forward to recipe detail JSP
            request.getRequestDispatcher("/user/recipe-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=Invalid recipe ID");
        } catch (Exception e) {
            System.err.println("Error loading recipe details: " + e.getMessage());
            response.sendRedirect("dashboard?error=Error loading recipe details");
        }
    }
}