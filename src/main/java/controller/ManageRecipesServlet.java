package controller;

import dao.RecipeDAO;
import model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing recipes (admin only)
 */
@WebServlet("/admin/manage-recipes")
public class ManageRecipesServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String view = request.getParameter("view");
        
        try {
            List<Recipe> recipes;
            
            if ("pending".equals(view)) {
                // Get only pending recipes
                recipes = recipeDAO.getPendingRecipes();
                request.setAttribute("viewType", "pending");
            } else {
                // Get all recipes
                recipes = recipeDAO.getAllRecipes();
                request.setAttribute("viewType", "all");
            }
            
            // Set attributes for JSP
            request.setAttribute("recipes", recipes);
            
            // Forward to manage recipes JSP
            request.getRequestDispatcher("/admin/manage-recipes.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading recipes: " + e.getMessage());
            request.setAttribute("error", "Error loading recipes");
            request.getRequestDispatcher("/admin/manage-recipes.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String recipeIdParam = request.getParameter("recipeId");
        
        if (action == null || recipeIdParam == null) {
            response.sendRedirect("manage-recipes?error=Invalid request");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            boolean success = false;
            String message = "";
            
            switch (action) {
                case "approve":
                    success = recipeDAO.updateRecipeStatus(recipeId, "APPROVED");
                    message = success ? "Recipe approved successfully" : "Failed to approve recipe";
                    break;
                    
                case "reject":
                    success = recipeDAO.updateRecipeStatus(recipeId, "REJECTED");
                    message = success ? "Recipe rejected successfully" : "Failed to reject recipe";
                    break;
                    
                case "delete":
                    success = recipeDAO.deleteRecipe(recipeId);
                    message = success ? "Recipe deleted successfully" : "Failed to delete recipe";
                    break;
                    
                default:
                    response.sendRedirect("manage-recipes?error=Invalid action");
                    return;
            }
            
            if (success) {
                response.sendRedirect("manage-recipes?success=" + message);
            } else {
                response.sendRedirect("manage-recipes?error=" + message);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("manage-recipes?error=Invalid recipe ID");
        } catch (Exception e) {
            System.err.println("Error managing recipe: " + e.getMessage());
            response.sendRedirect("manage-recipes?error=Error processing request");
        }
    }
}