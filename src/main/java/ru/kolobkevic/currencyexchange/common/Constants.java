package ru.kolobkevic.currencyexchange.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String DEFAULT_ERROR_MESSAGE = "Unknown server error";
    public static final String CURRENCY_EXISTS_MESSAGE = "Currency already exists";
    public static final String ILLEGAL_ARGUMENT_MESSAGE = "Illegal argument or arguments";
}
