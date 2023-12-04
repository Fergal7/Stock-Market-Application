package nl.rug.aoop.webview.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

/**
 * Encoder/decoder responsible for encoding/decoding objects to and from JSON.
 */
@Slf4j
public class JsonConverter {

    private static final Gson GSON = new GsonBuilder()
            .create();

    /**
     * Converts the provided object to JSON.
     * Note that records or fields such as LocalDateTime might not serialize properly.
     *
     * @param object The object to convert to JSON.
     * @return A JSON string representation of the provided object.
     */
    public String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Converts the provided string back to an Object.
     * Note that records or fields such as LocalDateTime might not deserialize properly.
     *
     * @param string A JSON representation of the object to convert.
     * @param clazz  The class of the object to convert to.
     * @param <T>    Type of the object encoded in the JSON string.
     * @return The object represented by the JSON string.
     */
    public <T> T fromJson(String string, Class<T> clazz) {
        try {
            return GSON.fromJson(string, clazz);
        } catch (JsonSyntaxException e) {
            log.error("Could not convert to object from json string", e);
        }
        return null;
    }
}
