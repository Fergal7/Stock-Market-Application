package nl.rug.aoop.webview;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.webview.routehandlers.HomeRouteHandler;
import nl.rug.aoop.webview.routehandlers.StockInfoHandler;
import nl.rug.aoop.webview.routehandlers.TraderInfoHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Simple HTTP server that accepts incoming HTTP requests for trader and stock information.
 * Provides the following routes:
 * <ul>
 *   <li>/traders</li>
 *   <li>/stocks</li>
 *   <li>/</li>
 * </ul>
 */
@Slf4j
public class StockMarketHttpServer {
    private HttpServer httpServer;

    private final StockExchangeDataModel stockExchange;
    @Getter
    private final int port;

    /**
     * Sets up the information needed to start an HTTP server.
     *
     * @param stockExchange The stock exchange from which this server can gather data when needed.
     * @param port          The port on which the server should be started.
     */
    public StockMarketHttpServer(StockExchangeDataModel stockExchange, int port) {
        this.stockExchange = stockExchange;
        this.port = port;
    }

    /**
     * Starts the stock market http server.
     */
    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            log.info("HTTP server accessible from http://localhost:" + httpServer.getAddress().getPort());
            httpServer.createContext("/traders", new TraderInfoHandler(stockExchange));
            httpServer.createContext("/stocks", new StockInfoHandler(stockExchange));
            httpServer.createContext("/", new HomeRouteHandler());
            httpServer.start();
        } catch (IOException e) {
            log.error("Failed to start HTTP server: ", e.getMessage());
        }
    }

    /**
     * Stops the stock market http server.
     */
    public void stop() {
        httpServer.stop(0);
    }

}