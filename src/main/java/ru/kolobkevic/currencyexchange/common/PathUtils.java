package ru.kolobkevic.currencyexchange.common;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public final class PathUtils {
    public static String getPathFromRequest(HttpServletRequest req) {
        return req.getPathInfo().replace("/", "").toUpperCase();
    }

    public static List<String> getListOfCurrenciesFromRequest(HttpServletRequest req) {
        String path = req.getPathInfo().replace("/", "").toUpperCase();
        return List.of(path.substring(0, 3), path.substring(3));
    }
    private PathUtils() {
    }
}
