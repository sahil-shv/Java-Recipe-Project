package controller;

import util.OpenAIService;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for AI-powered recipe recommendations
 */
@WebServlet("/user/ai-recipe")
public class AIRecipeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Forward to AI recipe form
        request.getRequestDispatcher("/user/ai-recipe.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredients = request.getParameter("ingredients");
        
        // Validate input
        if (ValidationUtil.isEmpty(ingredients)) {
            request.setAttribute("error", "Please enter at least one ingredient");
            request.getRequestDispatcher("/user/ai-recipe.jsp").forward(request, response);
            return;
        }
        
        // Sanitize input
        ingredients = ValidationUtil.sanitizeInput(ingredients.trim());
        
        // Validate ingredients format (should be ingredient names only)
        if (ingredients.length() < 3) {
            request.setAttribute("error", "Please enter valid ingredient names");
            request.getRequestDispatcher("/user/ai-recipe.jsp").forward(request, response);
            return;
        }
        
        try {
            // Generate recipe recommendation using OpenAI
            String recipeRecommendation = OpenAIService.generateRecipeRecommendation(ingredients);
            
            // Set attributes for JSP
            request.setAttribute("ingredients", ingredients);
            request.setAttribute("recipeRecommendation", recipeRecommendation);
            request.setAttribute("success", true);
            
            // Forward to AI recipe page with results
            request.getRequestDispatcher("/user/ai-recipe.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error generating AI recipe: " + e.getMessage());
            request.setAttribute("error", "Sorry, we couldn't generate a recipe recommendation at this time. Please try again later.");
            request.getRequestDispatcher("/user/ai-recipe.jsp").forward(request, response);
        }
    }
}