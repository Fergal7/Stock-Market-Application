package nl.rug.aoop.Stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of stocks.
 */
public class StockList {

    @JsonProperty("stocks")
    private Map<String, Stock> stocks;

    /**
     * Retrieves a list of all stocks.
     *
     * @return A list of stocks.
     */
    public List<Stock> getStocks(){
        return stocks.values().stream().toList();
    }

    /**
     * Sets the stocks map.
     *
     * @param stocks The map of stocks to set.
     */
    public void setStocks(Map<String, Stock> stocks) {
        this.stocks = stocks;
    }
}
