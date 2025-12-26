package controller;

import dao.CommentDAO;
import model.Comment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing comments (admin only)
 */
@WebServlet("/admin/manage-comments")
public class ManageCommentsServlet extends HttpServlet {
    
    private CommentDAO commentDAO;
    
    @Override
    public void init() throws ServletException {
        commentDAO = new CommentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get all comments
            List<Comment> comments = commentDAO.getAllComments();
            
            // Set attributes for JSP
            request.setAttribute("comments", comments);
            
            // Forward to manage comments JSP
            request.getRequestDispatcher("/admin/manage-comments.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading comments: " + e.getMessage());
            request.setAttribute("error", "Error loading comments");
            request.getRequestDispatcher("/admin/manage-comments.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String commentIdParam = request.getParameter("commentId");
        
        if (action == null || commentIdParam == null) {
            response.sendRedirect("manage-comments?error=Invalid request");
            return;
        }
        
        try {
            int commentId = Integer.parseInt(commentIdParam);
            
            if ("delete".equals(action)) {
                // Delete comment
                boolean success = commentDAO.deleteComment(commentId);
                
                if (success) {
                    response.sendRedirect("manage-comments?success=Comment deleted successfully");
                } else {
                    response.sendRedirect("manage-comments?error=Failed to delete comment");
                }
            } else {
                response.sendRedirect("manage-comments?error=Invalid action");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("manage-comments?error=Invalid comment ID");
        } catch (Exception e) {
            System.err.println("Error managing comment: " + e.getMessage());
            response.sendRedirect("manage-comments?error=Error processing request");
        }
    }
}