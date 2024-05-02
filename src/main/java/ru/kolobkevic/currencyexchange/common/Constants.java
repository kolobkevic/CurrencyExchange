package ru.kolobkevic.currencyexchange.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String DEFAULT_ERROR_MESSAGE = "Unknown server error";
    public static final String CURRENCY_EXISTS_MESSAGE = "Currency %s already exists";
    public static final String BAD_ARGUMENT_MESSAGE = "Bad argument or arguments. %s";
    public static final String CURRENCY_NOT_FOUND_MESSAGE = "Currency %s not found";
}
