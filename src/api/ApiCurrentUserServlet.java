package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/user/current")
public class ApiCurrentUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            JsonUtil.sendJsonResponse(resp, Map.of("user", null));
            return;
        }

        Object userObj = session.getAttribute("currentUser");
        if (userObj instanceof User) {
            User user = (User) userObj;
            Map<String, Object> response = new HashMap<>();
            response.put("user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "verified", user.isVerified(),
                "profileImage", user.getProfileImage() != null ? user.getProfileImage() : ""
            ));
            JsonUtil.sendJsonResponse(resp, response);
        } else {
            JsonUtil.sendJsonResponse(resp, Map.of("user", null));
        }
    }
}

