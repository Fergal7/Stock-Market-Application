package nl.rug.aoop.Stock;

import lombok.Setter;
import nl.rug.aoop.model.StockDataModel;

/**
 * Represents a stock with its associated information such as symbol, name, shares outstanding, and initial price.
 */
@Setter
public class Stock implements StockDataModel {

    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double initialPrice;

    /**
     * Default constructor for Stock.
     */
    public Stock() {}

    /**
     * Initializes a new instance of the Stock with given attributes.
     *
     * @param symbol            The symbol of the stock.
     * @param name              The name of the stock.
     * @param sharesOutstanding The number of shares outstanding for the stock.
     * @param price             The initial price of the stock.
     */
    public Stock(String symbol, String name, long sharesOutstanding, double price) {
        this.symbol = symbol;
        this.name = name;
        this.sharesOutstanding = sharesOutstanding;
        this.initialPrice = price;
    }

    /**
     * Retrieves the symbol of the stock.
     *
     * @return The stock symbol.
     */
    @Override
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Retrieves the name of the stock.
     *
     * @return The stock name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the number of shares outstanding for the stock.
     *
     * @return The number of shares outstanding.
     */
    @Override
    public long getSharesOutstanding() {
        return this.sharesOutstanding;
    }

    /**
     * Calculates and retrieves the market capitalization of the stock.
     *
     * @return The market capitalization value.
     */
    @Override
    public double getMarketCap() {
        return getSharesOutstanding() * getPrice();
    }

    /**
     * Retrieves the price of the stock.
     *
     * @return The stock price.
     */
    @Override
    public double getPrice() {
        return this.initialPrice;
    }
}
