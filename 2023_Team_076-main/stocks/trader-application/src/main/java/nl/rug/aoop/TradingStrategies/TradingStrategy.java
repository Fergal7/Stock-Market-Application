package nl.rug.aoop.TradingStrategies;

import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Traders.Trader;

/**
 * The TradingStrategy interface defines a contract for trading strategies that generate
 * buy and sell orders for traders in a stock exchange.
 */
public interface TradingStrategy {
    /**
     * Generates a buy or sell order for a trader based on a specific trading strategy.
     *
     * @param trader The trader for whom the order is generated.
     * @return An instance of the Order interface representing the buy or sell order, or null
     *         if the trading strategy does not generate an order for the trader.
     */
    Order generateOrder(Trader trader);
}

