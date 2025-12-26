package controller;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate input
        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            response.sendRedirect("login.jsp?error=Please fill in all fields");
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            response.sendRedirect("login.jsp?error=Please enter a valid email address");
            return;
        }
        
        try {
            // Find user by email
            User user = userDAO.findByEmail(email.trim().toLowerCase());
            
            if (user == null) {
                System.out.println("DEBUG: User not found for email: " + email);
                response.sendRedirect("login.jsp?error=Invalid email or password");
                return;
            }
            
            System.out.println("DEBUG: User found: " + user.getName() + ", Role: " + user.getRole());
            System.out.println("DEBUG: Stored hash: " + user.getPassword());
            System.out.println("DEBUG: Input password: " + password);
            
            // Verify password
            boolean passwordMatch = PasswordUtil.verifyPassword(password, user.getPassword());
            System.out.println("DEBUG: Password verification result: " + passwordMatch);
            
            if (!passwordMatch) {
                response.sendRedirect("login.jsp?error=Invalid email or password");
                return;
            }
            
            // Login successful - create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            // Redirect based on user role
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect("admin/dashboard");
            } else {
                response.sendRedirect("user/dashboard");
            }
            
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            response.sendRedirect("login.jsp?error=An error occurred during login. Please try again.");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            
            // Redirect to appropriate dashboard
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect("admin/dashboard");
            } else {
                response.sendRedirect("user/dashboard");
            }
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}