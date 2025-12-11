package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import utils.Logger;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(withContext(req, "/register.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isBlank(name) || isBlank(email) || isBlank(password)) {
            resp.sendRedirect(withContext(req, "/register.html?error=missing_fields"));
            return;
        }

        User user = new User(name.trim(), email.trim(), password);
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        boolean success = userService.registerUser(user, baseUrl);
        if (success) {
            resp.sendRedirect(withContext(req, "/login.html?registered=true"));
        } else {
            resp.sendRedirect(withContext(req, "/register.html?error=registration_failed"));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
