package nl.rug.aoop.webview.routehandlers;

import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.webview.data.TraderInfo;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Handles the /traders route. Responds with information of all traders in the provided stock exchange.
 */
@Slf4j
public class TraderInfoHandler extends JsonHttpHandler {

    private final StockExchangeDataModel stockExchange;

    private final Map<String, TraderInfo> traders;

    /**
     * Creates a new handles for the /traders route.
     *
     * @param stockExchange Stock exchange data model used to collect trader information to send.
     */
    public TraderInfoHandler(StockExchangeDataModel stockExchange) {
        this.stockExchange = stockExchange;
        traders = IntStream.range(0, stockExchange.getNumberOfTraders()).mapToObj(i -> {
            TraderDataModel trader = stockExchange.getTraderByIndex(i);
            return new TraderInfo(trader, getStockInfo());
        }).collect(Collectors.toMap(TraderInfo::getId, trader -> trader));
    }

    private Map<String, StockDataModel> getStockInfo() {
        return IntStream.range(0, stockExchange.getNumberOfStocks()).mapToObj(stockExchange::getStockByIndex)
                .collect(Collectors.toMap(StockDataModel::getSymbol, stock -> stock));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        IntStream.range(0, stockExchange.getNumberOfTraders()).forEach(i -> {
            TraderDataModel trader = stockExchange.getTraderByIndex(i);
            traders.get(trader.getId()).updateData(trader, getStockInfo());
        });

        respond(traders.values(), httpExchange);
    }
}
