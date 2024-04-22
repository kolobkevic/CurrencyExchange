package ru.kolobkevic.currencyexchange.common;

import jakarta.servlet.http.HttpServletRequest;

public final class PathUtils {
    public static String getPathFromRequest(HttpServletRequest req) {
        return req.getPathInfo().replace("/", "").toUpperCase();
    }

    private PathUtils() {
    }
}
