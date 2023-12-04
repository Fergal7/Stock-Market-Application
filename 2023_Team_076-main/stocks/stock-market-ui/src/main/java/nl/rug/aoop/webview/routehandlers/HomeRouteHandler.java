package nl.rug.aoop.webview.routehandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles the route / for the HTTP server. Simply sends back a 200 status code.
 */
public class HomeRouteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] emptyResponse = "".getBytes();
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, emptyResponse.length);
        final OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(emptyResponse);
        outputStream.close();
    }
}
