package nl.rug.aoop.webview.routehandlers;

import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.webview.data.StockInfo;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Handles the /stocks route. Responds with information of all stocks in the provided stock exchange.
 */
@Slf4j
public class StockInfoHandler extends JsonHttpHandler {

    private final StockExchangeDataModel stockExchange;
    private final Map<String, StockInfo> stocks;

    /**
     * Creates a new handles for the /stocks route.
     *
     * @param stockExchange Stock exchange data model used to collect stock information to send.
     */
    public StockInfoHandler(StockExchangeDataModel stockExchange) {
        this.stockExchange = stockExchange;
        stocks = IntStream.range(0, stockExchange.getNumberOfStocks()).mapToObj(i -> {
            StockDataModel stock = stockExchange.getStockByIndex(i);
            return new StockInfo(stock);
        }).collect(Collectors.toMap(StockInfo::getSymbol, stock -> stock));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        IntStream.range(0, stockExchange.getNumberOfStocks()).forEach(i -> {
            StockDataModel stock = stockExchange.getStockByIndex(i);
            stocks.get(stock.getSymbol()).updateData(stock);
        });
        respond(stocks.values(), httpExchange);
    }
}
