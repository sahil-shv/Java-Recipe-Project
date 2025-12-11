package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ViewRecipeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(withContext(req, "/recipes.html"));
            return;
        }

        String target = "/viewRecipe.html?id=" + idParam;
        resp.sendRedirect(withContext(req, target));
    }

    private String withContext(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }
}
