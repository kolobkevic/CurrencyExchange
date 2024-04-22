package ru.kolobkevic.currencyexchange.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    public static <T> String toJson(T obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON " + obj + ": " + e);
        }
    }

    private JsonUtils() {}
}
