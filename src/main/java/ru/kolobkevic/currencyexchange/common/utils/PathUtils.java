package ru.kolobkevic.currencyexchange.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.exceptions.BadArgumentException;

import java.util.List;

import static ru.kolobkevic.currencyexchange.common.Constants.BAD_ARGUMENT_MESSAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class PathUtils {
    public static final String CURRENCY_REGEX = "^[A-Z]{3}$";

    public static String getCurrencyFromRequest(HttpServletRequest req) throws BadArgumentException {
        String path = getPathFromRequest(req);
        if (isValidCurrencyString(path)) {
            return path;
        }
        throw new BadArgumentException(String.format(BAD_ARGUMENT_MESSAGE, req.getPathInfo()));
    }

    public static List<String> getListOfCurrenciesFromRequest(HttpServletRequest req) throws BadArgumentException {
        try {
            String path = getPathFromRequest(req);
            String baseCurrencyCode = path.substring(0, 3);
            String targetCurrencyCode = path.substring(3, 6);
            log.info("path: {}", path);
            if (isValidCurrencyString(baseCurrencyCode) && isValidCurrencyString(targetCurrencyCode) &&
                    path.length() == 6) {
                return List.of(baseCurrencyCode, targetCurrencyCode);
            }
            throw new BadArgumentException(String.format(BAD_ARGUMENT_MESSAGE, req.getPathInfo()));
        } catch (IndexOutOfBoundsException e) {
            throw new BadArgumentException(String.format(BAD_ARGUMENT_MESSAGE, req.getPathInfo()));
        }
    }

    public static void validateStringParams(String... args) throws BadArgumentException {
        for (String arg : args) {
            if (arg == null || arg.isEmpty()) {
                throw new BadArgumentException("Incorrect parameter");
            }
        }
    }

    private static String getPathFromRequest(HttpServletRequest req) {
        return req.getPathInfo().replace("/", "").toUpperCase();
    }

    private static boolean isValidCurrencyString(String currency) {
        return currency.matches(CURRENCY_REGEX);
    }
}
