package ru.kolobkevic.currencyexchange.common.exceptions;

public class MapperException extends RuntimeException {
    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
