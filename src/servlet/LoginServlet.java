package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;
import utils.Logger;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(withContext(req, "/login.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isBlank(email) || isBlank(password)) {
            resp.sendRedirect(withContext(req, "/login.html?error=missing_credentials"));
            return;
        }

        User user = userService.authenticate(email.trim(), password);
        if (user != null) {
            if (!userService.isEmailVerified(user)) {
                resp.sendRedirect(withContext(req, "/login.html?error=verify_email"));
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);
            resp.sendRedirect(withContext(req, "/recipes.html"));
        } else {
            resp.sendRedirect(withContext(req, "/login.html?error=invalid"));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
