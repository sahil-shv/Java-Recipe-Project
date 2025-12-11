package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.User;
import service.CommentService;

import java.io.IOException;

public class AddCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = null;
        if (session != null) {
            Object userObject = session.getAttribute("currentUser");
            if (userObject instanceof User) {
                currentUser = (User) userObject;
            }
        }
        String recipeIdParam = req.getParameter("recipeId");
        String commentText = req.getParameter("comment");

        if (recipeIdParam == null) {
            resp.sendRedirect(withContext(req, "/recipes"));
            return;
        }

        int recipeId;
        try {
            recipeId = Integer.parseInt(recipeIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(withContext(req, "/recipes.html"));
            return;
        }

        if (currentUser == null) {
            resp.sendRedirect(withContext(req, "/login.html?redirect=viewRecipe&id=" + recipeId));
            return;
        }

        if (commentText == null || commentText.trim().isEmpty()) {
            resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId + "&error=emptyComment"));
            return;
        }

        Comment comment = new Comment();
        comment.setRecipeId(recipeId);
        comment.setUserId(currentUser.getId());
        comment.setCommentText(commentText.trim());
        boolean saved = commentService.addComment(comment);
        if (saved) {
            resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId));
        } else {
            resp.sendRedirect(withContext(req, "/viewRecipe.html?id=" + recipeId + "&error=commentFailed"));
        }
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
