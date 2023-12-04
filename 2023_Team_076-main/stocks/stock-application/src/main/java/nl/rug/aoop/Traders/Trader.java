package nl.rug.aoop.Traders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.Transaction.Transaction;
import nl.rug.aoop.model.TraderDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a trader in this application.
 */
@Setter
@Getter
public class Trader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private Map<String, Integer> ownedStocks;
    private List<Transaction> transactionHistory = new ArrayList<>();
    private Map<String, Integer> outstandingStocks = new HashMap<>();
    private double outstandingFunds = 0;

    /**
     * Constructs a trader with the specified attributes.
     *
     * @param id           The unique identifier for the trader.
     * @param name         The name of the trader.
     * @param funds        The available funds of the trader.
     * @param ownedStocks  A map representing the stocks owned by the trader.
     */
    public Trader(String id, String name, double funds, Map<String, Integer> ownedStocks) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.ownedStocks = ownedStocks;
    }

    /**
     * Retrieves a list of stock names and their respective shares owned by the trader.
     *
     * @return A list of strings representing owned stocks and the number of shares owned.
     */
    @Override
    public List<String> getOwnedStocks() {
        ArrayList<String> stocks = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ownedStocks.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            stocks.add(key + ": " + value.toString());
        }
        return stocks;
    }

    /**
     * Retrieves the map of ownedStocks.
     *
     * @return A map of the currently owned stocks of the trader
     */
    public Map<String, Integer> getOwnedStocksMap() {
        return ownedStocks;
    }

    /**
     * Retrieves the number of shares owned by the trader for a specific stock.
     *
     * @param stockName The name of the stock.
     * @return The number of shares owned by the trader for the specified stock.
     */
    @Override
    public int getNumberOfOwnedShares(String stockName){
        return ownedStocks.getOrDefault(stockName, 0);
    }

    /**
     * Converts the trader object to a JSON string representation using Gson.
     *
     * @return A JSON representation of the trader object.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    /**
     * Creates a `Trader` object from a JSON string using Gson.
     *
     * @param traderString A JSON string representing a trader.
     * @return A `Trader` object created from the JSON string.
     */
    public static Trader createTraderFromString(String traderString) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.fromJson(traderString, Trader.class);
    }

    /**
     * Adds outstanding funds associated with open transactions.
     *
     * @param funds The amount of outstanding funds to add.
     */
    public void addOutstandingFunds(double funds) {
        this.outstandingFunds += funds;
    }

    /**
     * Removes outstanding funds associated with closed transactions.
     *
     * @param funds The amount of outstanding funds to remove.
     */
    public void removeOutstandingFunds(double funds) {
        this.outstandingFunds -= funds;
    }

    /**
     * Adds outstanding stocks associated with open transactions.
     *
     * @param stock  The name of the stock.
     * @param amount The number of shares to add to outstanding stocks.
     */
    public void addOutstandingStocks(String stock, int amount) {
        if (outstandingStocks.containsKey(stock)) {
            int currentValue = outstandingStocks.get(stock);
            int newValue = currentValue + amount;
            outstandingStocks.put(stock, newValue);
        } else {
            outstandingStocks.put(stock, amount);
        }
    }

    /**
     * Removes outstanding stocks associated with closed transactions.
     *
     * @param stock  The name of the stock.
     * @param amount The number of shares to remove from outstanding stocks.
     */
    public void removeOutstandingStock(String stock, int amount) {
        addOutstandingStocks(stock, -amount);
    }

    /**
     * Calculates the amount of funds available for trading (subtracting outstanding funds).
     *
     * @return The amount of funds available for trading.
     */
    public double getSpendableFunds() {
        return this.funds - this.outstandingFunds;
    }

    /**
     * Calculates the number of spendable shares of a specific stock
     * by subtracting outstanding shares from owned shares.
     *
     * @param stock The name of the stock.
     * @return The number of spendable shares for the specified stock.
     */
    public int getSpendableStocks(String stock) {
        return this.getOwnedStocksMap().getOrDefault(stock, 0)
                - this.getOutstandingStocks().getOrDefault(stock, 0);
    }

    /**
     * Adds a transaction to the trader's transaction history and updates funds and stock ownership accordingly.
     *
     * @param transaction The transaction to add to the history.
     */
    public void addTransaction(Transaction transaction) {
        if(transaction.getType().equals("Buy")) {
            this.funds -= transaction.getAmountSold() * transaction.getSharePrice();
            removeOutstandingFunds(transaction.getAmountSold() * transaction.getSharePrice());
            ownedStocks.merge(transaction.getStock(), transaction.getAmountSold(), Integer::sum);
        } else if(transaction.getType().equals("Sell")) {
            this.funds += transaction.getAmountSold() * transaction.getSharePrice();
            Integer newValue = ownedStocks.get(transaction.getStock()) - transaction.getAmountSold();
            ownedStocks.put(transaction.getStock(), newValue);
            removeOutstandingStock(transaction.getStock(), transaction.getAmountSold());
        }
        transactionHistory.add(transaction);
    }
}