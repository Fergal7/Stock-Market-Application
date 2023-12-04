package nl.rug.aoop.model;

/**
 * Data model of a single stock.
 * Note that a stock may have more properties; these are just the ones needed for the view.
 */
public interface StockDataModel {
    /**
     * Retrieves the symbol of a stock. Usually a 3-letter string of all upper-case letters.
     *
     * @return The symbol of the stock.
     */
    String getSymbol();

    /**
     * Retrieves the name of the company associated with the stock.
     *
     * @return Name of the company.
     */
    String getName();

    /**
     * Retrieves the number of shares available for trading.
     *
     * @return Total number of shares outstanding.
     */
    long getSharesOutstanding();

    /**
     * Retrieves the total market cap of the company. Calculated as sharesOutstanding * price
     *
     * @return Market cap of the company.
     */
    double getMarketCap();

    /**
     * Retrieves the price of a single share. Represents the latest price a share was traded at.
     *
     * @return The price of a single share.
     */
    double getPrice();
}
