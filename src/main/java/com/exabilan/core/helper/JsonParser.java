package com.exabilan.core.helper;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonParser {

    private JsonParser() {}

    private static final ObjectMapper objectMapper = new ObjectMapper().setVisibility(FIELD, ANY);

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
