package nl.rug.aoop.model;

/**
 * Data model of a stock exchange.
 * Note that a stock exchange may have more properties; these are just the ones needed for the view.
 */
public interface StockExchangeDataModel {
    /**
     * Retrieves a stock from the stock exchange by its index.
     *
     * @param index The index of the stock that should be accessed.
     * @return The stock at that index.
     */
    StockDataModel getStockByIndex(int index);

    /**
     * Retrieves the number of different stocks that can be traded on this stock exchange. Not to be confused with the
     * total number of shares.
     *
     * @return The total number of different stock symbols available at this stock exchange.
     */
    int getNumberOfStocks();

    /**
     * Retrieves a trader from the stock exchange by its index.
     *
     * @param index The index of the trader that should be accessed.
     * @return The trader at that index.
     */
    TraderDataModel getTraderByIndex(int index);

    /**
     * Retrieves the total number of traders trading on this stock exchange.
     *
     * @return The total number of traders trading on this stock exchange.
     */
    int getNumberOfTraders();
}
