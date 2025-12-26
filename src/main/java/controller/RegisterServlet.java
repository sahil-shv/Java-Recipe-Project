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
import java.io.IOException;

/**
 * Servlet for handling user registration
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (ValidationUtil.isEmpty(name) || ValidationUtil.isEmpty(email) || 
            ValidationUtil.isEmpty(password) || ValidationUtil.isEmpty(confirmPassword)) {
            response.sendRedirect("register.jsp?error=Please fill in all fields");
            return;
        }
        
        if (!ValidationUtil.isValidName(name)) {
            response.sendRedirect("register.jsp?error=Please enter a valid name (2-50 characters, letters only)");
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            response.sendRedirect("register.jsp?error=Please enter a valid email address");
            return;
        }
        
        if (!PasswordUtil.isValidPassword(password)) {
            response.sendRedirect("register.jsp?error=Password must be at least 6 characters and contain both letters and numbers");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.jsp?error=Passwords do not match");
            return;
        }
        
        try {
            // Check if email already exists
            if (userDAO.emailExists(email.trim().toLowerCase())) {
                response.sendRedirect("register.jsp?error=Email address is already registered");
                return;
            }
            
            // Create new user
            User user = new User();
            user.setName(ValidationUtil.sanitizeInput(name.trim()));
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(PasswordUtil.hashPassword(password));
            user.setRole("USER");
            
            int userId = userDAO.createUser(user);
            
            if (userId > 0) {
                response.sendRedirect("login.jsp?success=Registration successful! Please login with your credentials.");
            } else {
                response.sendRedirect("register.jsp?error=Registration failed. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            response.sendRedirect("register.jsp?error=An error occurred during registration. Please try again.");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Forward to registration page
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}