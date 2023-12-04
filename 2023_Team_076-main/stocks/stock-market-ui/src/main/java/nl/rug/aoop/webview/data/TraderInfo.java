package nl.rug.aoop.webview.data;

import lombok.Getter;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.TraderDataModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Data object that stores information about a trader in a straightforward manner.
 */
@Getter
public class TraderInfo {
    private static final int MAX_HISTORY_ITEMS = 50;

    private final String id;
    private final String name;
    private double funds;
    private double netWorth;
    private final double initialFunds;
    private final double initialNetWorth;

    private final LinkedList<MoneyInfo> fundsHistory;
    private final LinkedList<MoneyInfo> netWorthHistory;
    private Map<String, Integer> stockSymbols;

    /**
     * Creates a new information object from the provided data model.
     *
     * @param trader    Data model of the trader from which to create a new information object.
     * @param stockInfo A map that maps a stock symbol to its information.
     *                  The information contains, among others, the stock price for every stock.
     */
    public TraderInfo(TraderDataModel trader, Map<String, StockDataModel> stockInfo) {
        netWorthHistory = new LinkedList<>();
        fundsHistory = new LinkedList<>();
        this.id = trader.getId();
        this.name = trader.getName();
        this.stockSymbols = trader.getOwnedStocks().stream()
                .collect(
                        Collectors.toMap(symbol -> symbol, trader::getNumberOfOwnedShares)
                );
        setFunds(trader.getFunds());
        setNetWorth(calculateNetWorth(stockInfo));
        initialFunds = funds;
        initialNetWorth = netWorth;
    }

    /**
     * Calculates the net worth of this trader based on their owned stocks and the provided stock info.
     *
     * @param stockInfo A map that maps a stock symbol to its information.
     *                  The information contains, among others, the stock price for every stock.
     * @return The net worth of this trader.
     */
    private double calculateNetWorth(Map<String, StockDataModel> stockInfo) {
        double nw = funds;
        for (var entry : stockSymbols.entrySet()) {
            double stockPrice = 0;
            if (stockInfo.containsKey(entry.getKey())) {
                stockPrice = stockInfo.get(entry.getKey()).getPrice();
            }
            nw += stockPrice * entry.getValue();
        }
        return nw;
    }

    /**
     * Updates the balance of this trader. Also updates the balance history accordingly.
     *
     * @param newFunds The new balance of the trader.
     */
    public void setFunds(double newFunds) {
        fundsHistory.addLast(new MoneyInfo(newFunds));
        if (fundsHistory.size() > MAX_HISTORY_ITEMS) {
            fundsHistory.removeFirst();
        }
        this.funds = newFunds;
    }

    /**
     * Updates the net worth of this trader. Also updates the net worth history accordingly.
     *
     * @param newNetWorth The new net worth of this trader.
     */
    public void setNetWorth(double newNetWorth) {
        netWorthHistory.addLast(new MoneyInfo(newNetWorth));
        if (netWorthHistory.size() > MAX_HISTORY_ITEMS) {
            netWorthHistory.removeFirst();
        }
        this.netWorth = newNetWorth;
    }

    /**
     * Updates the stock portfolio, balance and net worth of this trader based on the information provided.
     *
     * @param trader    New trader information with which this info object should be updated.
     * @param stockInfo A map that maps a stock symbol to its information.
     *                  The information contains, among others, the stock price for every stock.
     */
    public void updateData(TraderDataModel trader, Map<String, StockDataModel> stockInfo) {
        this.stockSymbols = trader.getOwnedStocks().stream()
                .collect(
                        Collectors.toMap(symbol -> symbol, trader::getNumberOfOwnedShares)
                );
        setFunds(trader.getFunds());
        setNetWorth(calculateNetWorth(stockInfo));
    }
}
