package controller;

import dao.RecipeDAO;
import model.Recipe;
import model.User;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Servlet for adding new recipes
 */
@WebServlet("/user/add-recipe")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddRecipeServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    private static final String UPLOAD_DIR = "uploads";
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Forward to add recipe form
        request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String title = request.getParameter("title");
        String ingredients = request.getParameter("ingredients");
        String description = request.getParameter("description");
        
        // Validate input
        if (!ValidationUtil.isValidRecipeTitle(title)) {
            request.setAttribute("error", "Recipe title must be between 3 and 200 characters");
            request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
            return;
        }
        
        if (!ValidationUtil.isValidIngredients(ingredients)) {
            request.setAttribute("error", "Ingredients must be at least 10 characters long");
            request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
            return;
        }
        
        if (!ValidationUtil.isValidDescription(description)) {
            request.setAttribute("error", "Description must be at least 20 characters long");
            request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
            return;
        }
        
        try {
            // Handle file upload
            String imagePath = null;
            Part filePart = request.getPart("image");
            
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                // Validate file type
                if (!isValidImageFile(fileName)) {
                    request.setAttribute("error", "Please upload a valid image file (JPG, JPEG, PNG, GIF)");
                    request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
                    return;
                }
                
                // Generate unique filename
                String fileExtension = getFileExtension(fileName);
                String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                
                // Get upload directory path
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Save file
                String filePath = uploadPath + File.separator + uniqueFileName;
                filePart.write(filePath);
                
                // Store relative path for database
                imagePath = UPLOAD_DIR + "/" + uniqueFileName;
            }
            
            // Create recipe object
            Recipe recipe = new Recipe();
            recipe.setUserId(user.getUserId());
            recipe.setTitle(ValidationUtil.sanitizeInput(title.trim()));
            recipe.setIngredients(ValidationUtil.sanitizeInput(ingredients.trim()));
            recipe.setDescription(ValidationUtil.sanitizeInput(description.trim()));
            recipe.setImagePath(imagePath);
            recipe.setStatus("PENDING");
            
            // Save recipe
            int recipeId = recipeDAO.createRecipe(recipe);
            
            if (recipeId > 0) {
                response.sendRedirect("dashboard?success=Recipe added successfully! It will be visible after admin approval.");
            } else {
                request.setAttribute("error", "Failed to add recipe. Please try again.");
                request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error adding recipe: " + e.getMessage());
            request.setAttribute("error", "An error occurred while adding the recipe. Please try again.");
            request.getRequestDispatcher("/user/add-recipe.jsp").forward(request, response);
        }
    }
    
    /**
     * Check if uploaded file is a valid image
     * @param fileName File name
     * @return true if valid image file, false otherwise
     */
    private boolean isValidImageFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        
        String extension = getFileExtension(fileName).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || 
               extension.equals("png") || extension.equals("gif");
    }
    
    /**
     * Get file extension from filename
     * @param fileName File name
     * @return File extension
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        
        return "";
    }
}