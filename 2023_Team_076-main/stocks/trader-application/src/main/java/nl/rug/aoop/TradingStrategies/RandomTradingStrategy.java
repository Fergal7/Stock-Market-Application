package nl.rug.aoop.TradingStrategies;

import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Orders.SellOrder;
import nl.rug.aoop.Traders.Trader;

import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A trading strategy that generates random buy and sell orders for a trader.
 * The strategy randomly selects a stock, whether to buy or sell, the amount,
 * and the limit price based on the current market conditions.
 */
public class RandomTradingStrategy implements TradingStrategy {
    private final StockExchange stockExchange;

    /**
     * Creates a new RandomTradingStrategy with a reference to a StockExchange.
     *
     * @param stockExchange The StockExchange used to fetch market information.
     */
    public RandomTradingStrategy(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Generates a random buy or sell order for a trader.
     *
     * @param trader The trader for whom the order is generated.
     * @return A BuyOrder or SellOrder instance, or null if the trader cannot place an order.
     */
    @Override
    public Order generateOrder(Trader trader) {
        Random rand = new Random();
        boolean isBuy = rand.nextBoolean();

        List<String> availableStocks = stockExchange.getStocks().stream()
                .map(stock -> stock.getSymbol())
                .collect(Collectors.toList());

        String randomStock = availableStocks.get(rand.nextInt(availableStocks.size()));
        double currentPrice = stockExchange.getStockBySymbol(randomStock).getPrice();
        int randomAmount = 1 + rand.nextInt(5);
        int limitPrice = (int) (isBuy ? currentPrice * 1.05 : currentPrice * 0.95);

        if (isBuy && trader.getSpendableFunds() < limitPrice * randomAmount) {
            return null; // Not enough funds
        }

        if (!isBuy && trader.getSpendableStocks(randomStock) < randomAmount) {
            return null; // Doesn't own enough stocks to sell
        }

        return isBuy ? new BuyOrder(randomStock, randomAmount, limitPrice, trader.getId())
                : new SellOrder(randomStock, randomAmount, limitPrice, trader.getId());
    }
}
