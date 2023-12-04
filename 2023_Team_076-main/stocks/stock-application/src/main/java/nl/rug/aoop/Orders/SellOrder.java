package nl.rug.aoop.Orders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.Transaction.Transaction;

/**
 * SellOrder class. If someone wants to buy something of the stockmarket, they have to create a class like this.
 */
public class SellOrder extends Order {
    /**
     * Simple constructor for a SellOrder.
     *
     * @param stockName name of the stock you want to sell.
     * @param amount amount of stocks you want to sell.
     * @param price amount of money you want to give for the specific stock.
     * @param trader the trader that the SellOrder is from.
     */
    public SellOrder(String stockName, int amount, int price, String trader) {
        super(stockName, amount, price, trader);
    }

    /**
     * Method that is able to handle buyOrders with your sellOrder. Here we actually just
     * sell the stock to the given buyOrder.
     *
     * @param buyOrder buyOrder you want to buy the stocks from
     * @param stockExchange the stockchange with all the information about stocks and traders
     */
    @Override
    public void handle(Order buyOrder, StockExchange stockExchange) {
        int buyPrice = buyOrder.getPrice();
        int buyAmount = buyOrder.getAmount();
        int sellAmount = getAmount();
        Trader buyer = stockExchange.getTraderById(buyOrder.getTrader());
        Trader seller = stockExchange.getTraderById(this.getTrader());

        if (buyPrice < this.getPrice()) {
            return;
        }

        buyOrder.setAmount(buyAmount - sellAmount);
        this.setAmount(sellAmount - buyAmount);

        buyer.addTransaction(new Transaction("Buy", buyOrder.stockName, buyAmount - buyOrder.getAmount(), price));
        seller.addTransaction(new Transaction("Sell", buyOrder.stockName, buyAmount - buyOrder.getAmount(), price));

        stockExchange.getStockBySymbol(buyOrder.getStockName()).setInitialPrice(this.price);
    }

    /**
     * Function used to set the amount of the stock order. If the amount is zero, then the stock order is resolved.
     *
     * @param amount amount you want to set the BuyOrder amount to.
     */
    public void setAmount(int amount){
        if(amount <= 0){
            this.setResolved(true);
            this.amount = 0;
        } else {
            this.amount = amount;
        }
    }

    /**
     * Simple function to convert the SellOrder to Json.
     * @return a string representation of SellOrder
     */
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    /**
     * Simple method that lets you create a SellOrder from a json string.
     * @param messageString String containing a json of a BuyOrder.
     * @return a SellOrder just created from the parameter.
     */
    public static SellOrder createMessageFromString(String messageString) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.fromJson(messageString, SellOrder.class);
    }
}