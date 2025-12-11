package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RecipeListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryString = req.getQueryString();
        String target = "/recipes.html" + (queryString != null ? "?" + queryString : "");
        resp.sendRedirect(withContext(req, target));
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
