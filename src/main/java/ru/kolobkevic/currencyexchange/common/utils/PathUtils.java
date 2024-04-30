package ru.kolobkevic.currencyexchange.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class PathUtils {
    public static String getPathFromRequest(HttpServletRequest req) {
        return req.getPathInfo().replace("/", "").toUpperCase();
    }

    public static List<String> getListOfCurrenciesFromRequest(HttpServletRequest req) {
        String path = req.getPathInfo().replace("/", "").toUpperCase();
        String regex = "^[A-Z]+$";
        log.info("path: {}", path);
        if (path.length() != 6 || !path.matches(regex)) {
            throw new IllegalArgumentException("Incorrect path: " + path);
        }
        return List.of(path.substring(0, 3), path.substring(3));
    }
}
