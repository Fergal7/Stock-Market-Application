package nl.rug.aoop.Orders;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.StockExchange.StockExchange;

/**
 * Abstract representation of an order in the stock exchange system.
 */
@Getter
@Setter
public abstract class Order {
    /** ID or name of the trader initiating the order. */
    protected String trader;

    /** Name of the stock associated with the order. */
    protected String stockName;

    /** Number of stock units in the order. */
    protected int amount;

    /** Price per unit of stock in the order. */
    protected int price;

    /** Flag indicating if the order has been resolved or completed. */
    protected boolean resolved;

    /** Default constructor. */
    public Order() { }

    /**
     * Initializes a new instance of Order.
     *
     * @param stockName Name of the stock associated with the order.
     * @param amount Number of stock units in the order.
     * @param price Price per unit of stock in the order.
     * @param trader ID or name of the trader initiating the order.
     */
    public Order(String stockName, int amount, int price, String trader) {
        this.stockName = stockName;
        this.amount = amount;
        this.price = price;
        this.trader = trader;
        this.resolved = false;
    }

    /**
     * Abstract method to handle the order logic.
     *
     * @param buyOrder The opposing order to match against.
     * @param stockExchange Reference to the stock exchange system.
     */
    public abstract void handle(Order buyOrder, StockExchange stockExchange);

    /**
     * Converts the Order object to its JSON representation.
     * This method is to be overridden by derived classes.
     *
     * @return JSON string representation of the Order (default is null).
     */
    public String toJson(){
        return null;
    }
}
