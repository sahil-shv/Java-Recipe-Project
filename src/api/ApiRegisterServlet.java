package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class ApiRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isBlank(name) || isBlank(email) || isBlank(password)) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "All fields are required");
            return;
        }

        try {
            String baseUrl = req.getScheme() + "://" + req.getServerName() + 
                           (req.getServerPort() != 80 && req.getServerPort() != 443 ? ":" + req.getServerPort() : "") + 
                           req.getContextPath();
            
            User user = new User(name.trim(), email.trim(), password);
            boolean success = userService.registerUser(user, baseUrl);
            
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Registration successful! Please check your email for verification.");
                JsonUtil.sendJsonResponse(resp, response);
            } else {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    "Unable to register. Email may already be taken or password too short.");
            }
        } catch (Exception e) {
            utils.Logger.error("Error during registration", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Registration failed");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

