package filter;

import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication filter to protect user and admin areas
 */
@WebFilter(urlPatterns = {"/user/*", "/admin/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Get the path after context path
        String path = requestURI.substring(contextPath.length());
        
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        // Check if user is logged in
        if (user == null) {
            // User not logged in, redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp?error=Please login to access this page");
            return;
        }
        
        // Check role-based access
        if (path.startsWith("/admin/")) {
            // Admin area - check if user is admin
            if (!"ADMIN".equals(user.getRole())) {
                // User is not admin, redirect to user dashboard or show error
                httpResponse.sendRedirect(contextPath + "/user/dashboard?error=Access denied. Admin privileges required.");
                return;
            }
        } else if (path.startsWith("/user/")) {
            // User area - check if user is regular user or admin
            if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
                // Invalid role, redirect to login
                httpResponse.sendRedirect(contextPath + "/login.jsp?error=Invalid user role");
                return;
            }
        }
        
        // User is authenticated and authorized, continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}