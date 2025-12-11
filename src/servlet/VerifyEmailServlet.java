package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            resp.sendRedirect(buildRedirectUrl(req, false, "Invalid verification link."));
            return;
        }

        boolean verified = userService.verifyEmail(token);
        String message = verified
                ? "Email verified successfully! You can now log in."
                : "Verification failed. The link may be invalid or expired.";

        resp.sendRedirect(buildRedirectUrl(req, verified, message));
    }

    private String buildRedirectUrl(HttpServletRequest req, boolean success, String message) {
        String status = success ? "success" : "error";
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return withContext(req, "/verification.html?status=" + status + "&message=" + encodedMessage);
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}

