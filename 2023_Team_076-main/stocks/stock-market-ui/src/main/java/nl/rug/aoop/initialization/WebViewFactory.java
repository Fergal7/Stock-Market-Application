package nl.rug.aoop.initialization;

import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.webview.StockMarketHttpServer;

/**
 * Creates a communication point for the web UI for the provided stock exchange. Note that this class in itself
 * does not spawn the web UI.
 */
public class WebViewFactory implements AbstractViewFactory {
    /**
     * Fall back port if the environment variable is not available.
     */
    private static final int VIEW_HTTP_PORT = 29385;

    @Override
    public void createView(StockExchangeDataModel stockExchangeDataModel) {

        int viewHttpPort;
        try {
            viewHttpPort = Integer.parseInt(System.getenv("VIEW_HTTP_PORT"));
        } catch (NumberFormatException e) {
            viewHttpPort = VIEW_HTTP_PORT;
        }
        StockMarketHttpServer server = new StockMarketHttpServer(stockExchangeDataModel, viewHttpPort);
        server.start();
    }
}
