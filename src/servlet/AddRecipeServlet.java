package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import model.Recipe;
import model.User;
import service.RecipeService;
import utils.ImageCompressor;
import utils.Logger;

import java.io.File;
import java.io.IOException;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max
public class AddRecipeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RecipeService recipeService = new RecipeService();
    private static final String UPLOAD_DIR = "uploads" + File.separator + "recipes" + File.separator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getAuthenticatedUser(req.getSession(false));
        if (currentUser == null) {
            resp.sendRedirect(withContext(req, "/login.html?redirect=addRecipe"));
            return;
        }
        resp.sendRedirect(withContext(req, "/addRecipe.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        if (currentUser == null) {
            resp.sendRedirect(withContext(req, "/login.html?redirect=addRecipe"));
            return;
        }

        String title = req.getParameter("title");
        String ingredients = req.getParameter("ingredients");
        String steps = req.getParameter("steps");
        String category = req.getParameter("category");
        String imageUrl = req.getParameter("image"); // For URL input

        if (isBlank(title) || isBlank(ingredients) || isBlank(steps)) {
            resp.sendRedirect(withContext(req, "/addRecipe.html?error=missing_fields"));
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title.trim());
        recipe.setIngredients(ingredients.trim());
        recipe.setSteps(steps.trim());
        recipe.setCategory(isBlank(category) ? null : category.trim());
        recipe.setUserId(currentUser.getId());

        // Handle file upload
        try {
            Part filePart = req.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                String appPath = req.getServletContext().getRealPath("");
                String uploadPath = appPath + UPLOAD_DIR;
                
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File uploadFile = new File(uploadPath + fileName);
                filePart.write(uploadFile.getAbsolutePath());

                // Compress image
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                File compressedFile = ImageCompressor.compressImage(uploadFile, 
                    new File(uploadPath + "compressed_" + fileName), fileExtension);
                
                String imagePath = req.getContextPath() + "/" + UPLOAD_DIR + compressedFile.getName();
                recipe.setImage(imagePath);
            } else if (!isBlank(imageUrl)) {
                recipe.setImage(imageUrl.trim());
            }
        } catch (Exception e) {
            Logger.error("Error processing image upload", e);
        }

        boolean success = recipeService.addRecipe(recipe);
        if (success) {
            resp.sendRedirect(withContext(req, "/recipes.html?added=true"));
        } else {
            resp.sendRedirect(withContext(req, "/addRecipe.html?error=save_failed"));
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
