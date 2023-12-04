package nl.rug.aoop.StockExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;

import java.util.List;

/**
 * Represents a stock exchange containing a list of stocks and traders.
 */
@Getter
public class StockExchange implements StockExchangeDataModel {
    private final List<Stock> stocks;
    private final List<Trader> traders;

    /**
     * Constructs a StockExchange instance with specified stocks and traders.
     *
     * @param stocks   The list of stocks in the exchange.
     * @param traders  The list of traders in the exchange.
     */
    public StockExchange(List<Stock> stocks, List<Trader> traders) {
        this.stocks = stocks;
        this.traders = traders;
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        return stocks != null ? stocks.get(index) : null;
    }

    @Override
    public int getNumberOfStocks() {
        return stocks != null ? stocks.size() : 0;
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return traders != null ? traders.get(index) : null;
    }

    @Override
    public int getNumberOfTraders() {
        return traders != null ? traders.size() : 0;
    }

    /**
     * Retrieves a stock by its symbol.
     *
     * @param symbol The stock symbol.
     * @return The stock with the specified symbol, or null if not found.
     */
    public Stock getStockBySymbol(String symbol) {
        return stocks != null ? stocks.stream().filter(stock ->
                stock.getSymbol().equals(symbol)).findFirst().orElse(null) : null;
    }

    /**
     * Retrieves a trader by their ID.
     *
     * @param id The trader's ID.
     * @return The trader with the specified ID, or null if not found.
     */
    public Trader getTraderById(String id) {
        return traders != null ? traders.stream().filter(trader
                -> trader.getId().equals(id)).findFirst().orElse(null) : null;
    }

    /**
     * Serializes the StockExchange instance into a JSON string.
     *
     * @return A JSON string representation of this StockExchange.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    /**
     * Deserializes a JSON string into a StockExchange instance.
     *
     * @param messageString The JSON string representing a StockExchange.
     * @return A StockExchange instance, or null if deserialization fails.
     */
    public static StockExchange createSEFromString(String messageString) {
        Gson gson = new GsonBuilder().create();
        StockExchange stockExchange = null;
        try {
            stockExchange = gson.fromJson(messageString, StockExchange.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Error deserializing JSON: " + e.getMessage());
        }
        return stockExchange;
    }
}
