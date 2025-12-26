package controller;

import dao.CommentDAO;
import model.Comment;
import model.User;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for adding comments to recipes
 */
@WebServlet("/user/add-comment")
public class AddCommentServlet extends HttpServlet {
    
    private CommentDAO commentDAO;
    
    @Override
    public void init() throws ServletException {
        commentDAO = new CommentDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String recipeIdParam = request.getParameter("recipeId");
        String commentText = request.getParameter("comment");
        
        if (recipeIdParam == null || !ValidationUtil.isValidComment(commentText)) {
            response.sendRedirect("dashboard?error=Invalid comment or recipe");
            return;
        }
        
        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            
            // Create comment object
            Comment comment = new Comment();
            comment.setUserId(user.getUserId());
            comment.setRecipeId(recipeId);
            comment.setComment(ValidationUtil.sanitizeInput(commentText.trim()));
            
            // Save comment
            int commentId = commentDAO.addComment(comment);
            
            if (commentId > 0) {
                response.sendRedirect("recipe-detail?id=" + recipeId + "&success=Comment added successfully");
            } else {
                response.sendRedirect("recipe-detail?id=" + recipeId + "&error=Failed to add comment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard?error=Invalid recipe ID");
        } catch (Exception e) {
            System.err.println("Error adding comment: " + e.getMessage());
            response.sendRedirect("dashboard?error=Error adding comment");
        }
    }
}