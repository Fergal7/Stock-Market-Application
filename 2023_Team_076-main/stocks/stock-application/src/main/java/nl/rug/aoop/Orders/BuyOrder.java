package nl.rug.aoop.Orders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.Transaction.Transaction;

/**
 * BuyOrder class. If someone wants to buy something of the stockmarket, they have to create a class like this.
 */
public class BuyOrder extends Order {
    /**
     * Simple constructor for a BuyOrder.
     *
     * @param stockName name of the stock you want to buy.
     * @param amount amount of stocks you want to buy.
     * @param price amount of money you want to give for the specific stock.
     * @param trader the trader that the BuyOrder is from.
     */
    public BuyOrder(String stockName, int amount, int price, String trader) {
        super(stockName, amount, price, trader);
    }

    /**
     * Method that is able to handle sellOrders with your BuyOrder. Here we actually just
     * buy the stock from the given sellOrder.
     *
     * @param sellOrder sellOrder you want to buy the stocks from
     * @param stockExchange the stockchange with all the information about stocks and traders
     */
    @Override
    public void handle(Order sellOrder, StockExchange stockExchange) {
        int sellPrice = sellOrder.getPrice();
        int sellAmount = sellOrder.getAmount();
        Trader buyer = stockExchange.getTraderById(this.trader);
        Trader seller = stockExchange.getTraderById(sellOrder.getTrader());

        if (sellPrice > this.getPrice()) {
            return;
        }

        sellOrder.setAmount(sellAmount - this.amount);
        this.setAmount(this.amount - sellAmount);

        buyer.addTransaction(new Transaction("Buy", sellOrder.stockName, sellAmount-sellOrder.getAmount(), price));
        seller.addTransaction(new Transaction("Sell", sellOrder.stockName, sellAmount-sellOrder.getAmount(), price));

        stockExchange.getStockBySymbol(sellOrder.getStockName()).setInitialPrice(this.getPrice());
    }

    /**
     * Function used to set the amount of the stock order. If the amount is zero, then the stock order is resolved.
     *
     * @param amount amount you want to set the BuyOrder amount to.
     */
    @Override
    public void setAmount(int amount){
        if(amount <= 0){
            this.setResolved(true);
            this.amount = 0;
        } else {
            this.amount = amount;
        }
    }

    /**
     * Simple function to convert the BuyOrder to Json.
     * @return a string representation of BuyOrder
     */
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    /**
     * Simple method that lets you create a BuyOrder from a json string.
     * @param messageString String containing a json of a BuyOrder.
     * @return a BuyOrder just created from the parameter.
     */
    public static BuyOrder createBuyOrderFromString(String messageString) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.fromJson(messageString, BuyOrder.class);
    }
}