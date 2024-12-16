package pt.psoft.g1.psoftg1.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
public class JsonHelper {

    public static <T> String toJson(final ObjectMapper objectMapper, final T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(final ObjectMapper objectMapper, final String payload, final Class<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }

    public static <T> T fromJson(final ObjectMapper objectMapper, final String payload, final TypeReference<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }

}