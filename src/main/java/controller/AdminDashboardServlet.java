package controller;

import dao.CommentDAO;
import dao.LikeDAO;
import dao.RecipeDAO;
import dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for admin dashboard with analytics
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private RecipeDAO recipeDAO;
    private LikeDAO likeDAO;
    private CommentDAO commentDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        recipeDAO = new RecipeDAO();
        likeDAO = new LikeDAO();
        commentDAO = new CommentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get dashboard analytics
            int totalUsers = userDAO.getTotalUserCount();
            int totalRecipes = recipeDAO.getTotalRecipeCount();
            int pendingRecipes = recipeDAO.getPendingRecipeCount();
            int totalLikes = likeDAO.getTotalLikeCount();
            int totalComments = commentDAO.getTotalCommentCount();
            
            // Set attributes for JSP
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalRecipes", totalRecipes);
            request.setAttribute("pendingRecipes", pendingRecipes);
            request.setAttribute("totalLikes", totalLikes);
            request.setAttribute("totalComments", totalComments);
            
            // Forward to admin dashboard JSP
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard: " + e.getMessage());
            request.setAttribute("error", "Error loading dashboard analytics");
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}