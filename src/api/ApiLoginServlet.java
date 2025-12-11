package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isBlank(email) || isBlank(password)) {
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Email and password are required");
            return;
        }

        try {
            User user = userService.authenticate(email.trim(), password);
            if (user != null) {
                if (!userService.isEmailVerified(user)) {
                    JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, "Please verify your email before logging in. Check your inbox for the verification link.");
                    return;
                }

                HttpSession session = req.getSession(true);
                session.setAttribute("currentUser", user);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("user", Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "verified", user.isVerified(),
                    "profileImage", user.getProfileImage() != null ? user.getProfileImage() : ""
                ));
                response.put("message", "Login successful");
                
                JsonUtil.sendJsonResponse(resp, response);
            } else {
                JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
            }
        } catch (Exception e) {
            utils.Logger.error("Error during login", e);
            JsonUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login failed");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

