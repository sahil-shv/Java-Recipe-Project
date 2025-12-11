package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JsonUtil;
import utils.Logger;

import java.io.IOException;

public class ExceptionHandlerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.info("ExceptionHandlerFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            Logger.error("Unhandled exception in filter chain: " + httpRequest.getRequestURI(), e);
            
            boolean wantsJson = httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/api")
                    || (httpRequest.getHeader("Accept") != null
                        && httpRequest.getHeader("Accept").contains("application/json"));

            if (wantsJson) {
                JsonUtil.sendErrorResponse(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
                return;
            }

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/error.html");
        }
    }

    @Override
    public void destroy() {
        Logger.info("ExceptionHandlerFilter destroyed");
    }
}

