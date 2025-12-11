package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.User;
import service.CommentService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/comments")
public class ApiAddCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = getAuthenticatedUser(session);
        
        if (currentUser == null) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        String recipeIdParam = req.getParameter("recipeId");
        String commentText = req.getParameter("comment");

        if (recipeIdParam == null) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Recipe ID required");
            return;
        }

        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            
            if (commentText == null || commentText.trim().isEmpty()) {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Comment cannot be empty");
                return;
            }

            Comment comment = new Comment();
            comment.setRecipeId(recipeId);
            comment.setUserId(currentUser.getId());
            comment.setCommentText(commentText.trim());
            
            boolean saved = commentService.addComment(comment);
            if (saved) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Comment added successfully");
                JsonUtil.sendJsonResponse(resp, response);
            } else {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add comment");
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe ID");
        } catch (Exception e) {
            utils.Logger.error("Error adding comment", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding comment");
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
}

