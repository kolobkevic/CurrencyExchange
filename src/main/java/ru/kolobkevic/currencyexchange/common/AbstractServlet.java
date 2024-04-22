package ru.kolobkevic.currencyexchange.common;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class AbstractServlet extends HttpServlet {
    protected <T> void sendJsonResponse(HttpServletResponse resp, Integer statusCode, T jsonResponse)
            throws IOException {
        resp.getWriter().write(JsonUtils.toJson(jsonResponse));
        resp.setStatus(statusCode);
    }
}
