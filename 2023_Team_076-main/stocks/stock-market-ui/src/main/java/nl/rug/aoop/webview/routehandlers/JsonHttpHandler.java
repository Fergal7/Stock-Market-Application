package nl.rug.aoop.webview.routehandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.webview.util.JsonConverter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Handler for HTTP requests that require a JSON response.
 */
@Slf4j
public abstract class JsonHttpHandler implements HttpHandler {
    private static final JsonConverter JSON_CONVERTER = new JsonConverter();
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String CONTENT_TYPE = "application/json";

    /**
     * Respond to the request with the provided info. Has support for CORS.
     *
     * @param info         Info object to respond. Will be converted to JSON by this method.
     *                     The JSON conversion is done automatically and does not support custom formats,
     *                     so be careful with passing records or LocalDateTime fields.
     * @param httpExchange Exchange object used to send a response.
     * @throws IOException Thrown when the response failed.
     */
    protected void respond(Object info, HttpExchange httpExchange) throws IOException {
        String json = JSON_CONVERTER.toJson(info);
        byte[] contentBytes = json.getBytes(CHARSET);
        httpExchange.getResponseHeaders().set("Content-Type", CONTENT_TYPE);
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
        }
        httpExchange.sendResponseHeaders(200, contentBytes.length);
        final OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(contentBytes);
        outputStream.close();
    }
}
