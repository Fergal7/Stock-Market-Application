package nl.rug.aoop.Commands;

import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Orders.SellOrder;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.command.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Represents a command to execute a limit sell order in the stock exchange.
 */
public class LimitSellCommand implements Command {
    private final ArrayList<BuyOrder> bids;
    private final ArrayList<SellOrder> asks;
    private final StockExchange stockExchange;

    /**
     * Initializes a new instance of the LimitSellCommand.
     *
     * @param bids           List of buy orders.
     * @param asks           List of sell orders.
     * @param stockExchange  The stock exchange system.
     */
    public LimitSellCommand(ArrayList<BuyOrder> bids, ArrayList<SellOrder> asks, StockExchange stockExchange) {
        this.bids = bids;
        this.asks = asks;
        this.stockExchange = stockExchange;
    }

    /**
     * Executes the sell order. Matches the sell order against existing buy orders.
     * If a match is found, updates the orders and the stock exchange accordingly.
     *
     * @param params Parameters required for execution, including the order details.
     */
    @Override
    public void execute(Map<String, Object> params) {
        SellOrder sellOrder = SellOrder.createMessageFromString((String)params.get("OrderJson"));
        asks.add(sellOrder);
        stockExchange.getTraderById(sellOrder.getTrader())
                .addOutstandingStocks(sellOrder.getStockName(), sellOrder.getAmount());

        String stock = sellOrder.getStockName();
        List<BuyOrder> specific_orders = new ArrayList<>(
                this.bids.stream().filter(x -> x.getStockName().equals(stock) && !x.isResolved()
                        && !x.getTrader().equals(sellOrder.getTrader())).toList());
        specific_orders.sort(Comparator.comparingInt(BuyOrder::getPrice).reversed());

        specific_orders.forEach(buyOrder -> {
            if(!sellOrder.isResolved()) {
                buyOrder.handle(sellOrder, stockExchange);
            }
        });

        bids.removeIf(Order::isResolved);
        asks.removeIf(Order::isResolved);
    }
}
