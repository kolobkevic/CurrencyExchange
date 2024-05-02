package ru.kolobkevic.currencyexchange.common.exceptions;

public class BadArgumentException extends Exception {
    public BadArgumentException(String message) {
        super(message);
    }

    public BadArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
