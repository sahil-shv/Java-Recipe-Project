package controller;

import dao.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing users (admin only)
 */
@WebServlet("/admin/manage-users")
public class ManageUsersServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get all users
            List<User> users = userDAO.getAllUsers();
            
            // Set attributes for JSP
            request.setAttribute("users", users);
            
            // Forward to manage users JSP
            request.getRequestDispatcher("/admin/manage-users.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            request.setAttribute("error", "Error loading users");
            request.getRequestDispatcher("/admin/manage-users.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");
        
        if (action == null || userIdParam == null) {
            response.sendRedirect("manage-users?error=Invalid request");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdParam);
            
            if ("delete".equals(action)) {
                // Delete user
                boolean success = userDAO.deleteUser(userId);
                
                if (success) {
                    response.sendRedirect("manage-users?success=User deleted successfully");
                } else {
                    response.sendRedirect("manage-users?error=Failed to delete user");
                }
            } else {
                response.sendRedirect("manage-users?error=Invalid action");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("manage-users?error=Invalid user ID");
        } catch (Exception e) {
            System.err.println("Error managing user: " + e.getMessage());
            response.sendRedirect("manage-users?error=Error processing request");
        }
    }
}