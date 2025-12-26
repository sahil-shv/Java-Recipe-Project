package controller;

import dao.RecipeDAO;
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
 * Servlet for user dashboard
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        try {
            // Get approved recipes with like status for current user
            List<Recipe> approvedRecipes = recipeDAO.getApprovedRecipes(user.getUserId());
            
            // Get user's own recipes
            List<Recipe> userRecipes = recipeDAO.getRecipesByUserId(user.getUserId());
            
            // Set attributes for JSP
            request.setAttribute("approvedRecipes", approvedRecipes);
            request.setAttribute("userRecipes", userRecipes);
            request.setAttribute("user", user);
            
            // Forward to user dashboard JSP
            request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading user dashboard: " + e.getMessage());
            request.setAttribute("error", "Error loading dashboard. Please try again.");
            request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
        }
    }
}