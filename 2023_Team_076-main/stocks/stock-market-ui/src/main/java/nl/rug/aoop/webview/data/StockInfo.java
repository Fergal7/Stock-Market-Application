package nl.rug.aoop.webview.data;

import lombok.Getter;
import nl.rug.aoop.model.StockDataModel;

import java.util.LinkedList;

/**
 * Data object that stores information about a stock in a straightforward manner.
 */
@Getter
public class StockInfo {

    private static final int MAX_HISTORY_ITEMS = 50;
    private final String symbol;
    private final String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private final double initialPrice;
    private double lastPrice;

    // Use linkedList for addLast method support
    private final LinkedList<MoneyInfo> priceHistory;

    /**
     * Creates a new information object from the provided data model.
     *
     * @param stock Data model of the stock from which to create a new information object.
     */
    public StockInfo(StockDataModel stock) {
        priceHistory = new LinkedList<>();
        this.symbol = stock.getSymbol();
        this.name = stock.getName();
        this.sharesOutstanding = stock.getSharesOutstanding();
        this.marketCap = stock.getMarketCap();
        setPrice(stock.getPrice());
        initialPrice = price;
        lastPrice = price;
    }

    /**
     * Updates the current price of the stock. Also updates the history and last price accordingly.
     *
     * @param newPrice The new price of the stock.
     */
    public void setPrice(double newPrice) {
        if (price != newPrice) {
            lastPrice = price;
        }
        priceHistory.addLast(new MoneyInfo(newPrice));
        if (priceHistory.size() > MAX_HISTORY_ITEMS) {
            priceHistory.removeFirst();
        }
        price = newPrice;
    }

    /**
     * Updates the sharesOutstanding, market cap and price of the stock according to the data provided.
     *
     * @param stock New stock information with which this info object should be updated.
     */
    public void updateData(StockDataModel stock) {
        this.sharesOutstanding = stock.getSharesOutstanding();
        this.marketCap = stock.getMarketCap();
        setPrice(stock.getPrice());
    }
}
