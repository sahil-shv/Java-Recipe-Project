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
import service.UserService;
import utils.ImageCompressor;
import utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // 2MB max
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();
    private final RecipeService recipeService = new RecipeService();
    private static final String UPLOAD_DIR = "uploads" + File.separator + "profile" + File.separator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        if (currentUser == null) {
            resp.sendRedirect(withContext(req, "/login.html?redirect=profile"));
            return;
        }

        // Get full user details
        User fullUser = userService.getUserById(currentUser.getId());
        if (fullUser == null) {
            resp.sendRedirect(withContext(req, "/login.html"));
            return;
        }

        // Get user's recipes
        List<Recipe> userRecipes = recipeService.getRecipesByUserId(fullUser.getId());

        // Get statistics
        int recipeCount = userService.getRecipeCount(fullUser.getId());
        double avgRating = userService.getAverageRatingOfRecipes(fullUser.getId());

        req.setAttribute("user", fullUser);
        req.setAttribute("recipes", userRecipes);
        req.setAttribute("recipeCount", recipeCount);
        req.setAttribute("avgRating", avgRating);
        resp.sendRedirect(withContext(req, "/profile.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        if (currentUser == null) {
            resp.sendRedirect(withContext(req, "/login.html?redirect=profile"));
            return;
        }

        String action = req.getParameter("action");
        
        if ("updateProfileImage".equals(action)) {
            try {
                Part filePart = req.getPart("profileImage");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = currentUser.getId() + "_" + System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
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
                    
                    if (userService.updateProfileImage(currentUser.getId(), imagePath)) {
                        // Update session user
                        User updatedUser = userService.getUserById(currentUser.getId());
                        if (updatedUser != null) {
                            session.setAttribute("currentUser", updatedUser);
                        }
                        resp.sendRedirect(withContext(req, "/profile.html?success=imageUpdated"));
                    } else {
                        resp.sendRedirect(withContext(req, "/profile.html?error=imageUpdateFailed"));
                    }
                } else {
                    resp.sendRedirect(withContext(req, "/profile.html?error=noFile"));
                }
            } catch (Exception e) {
                Logger.error("Error updating profile image", e);
                resp.sendRedirect(withContext(req, "/profile.html?error=imageUpdateFailed"));
            }
        } else if ("updatePassword".equals(action)) {
            String oldPassword = req.getParameter("oldPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            if (oldPassword == null || newPassword == null || confirmPassword == null) {
                resp.sendRedirect(withContext(req, "/profile.html?error=missingFields"));
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                resp.sendRedirect(withContext(req, "/profile.html?error=passwordMismatch"));
                return;
            }

            if (userService.updatePassword(currentUser.getId(), oldPassword, newPassword)) {
                resp.sendRedirect(withContext(req, "/profile.html?success=passwordUpdated"));
            } else {
                resp.sendRedirect(withContext(req, "/profile.html?error=passwordUpdateFailed"));
            }
        } else {
            resp.sendRedirect(withContext(req, "/profile.html"));
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

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}

