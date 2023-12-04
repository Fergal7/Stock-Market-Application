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
 * Represents a command to execute a limit buy order in the stock exchange.
 */
public class LimitBuyCommand implements Command {
    private final ArrayList<BuyOrder> bids;
    private final ArrayList<SellOrder> asks;
    private final StockExchange stockExchange;

    /**
     * Initializes a new instance of the LimitBuyCommand.
     *
     * @param bids           List of buy orders.
     * @param asks           List of sell orders.
     * @param stockExchange  The stock exchange system.
     */
    public LimitBuyCommand(ArrayList<BuyOrder> bids, ArrayList<SellOrder> asks, StockExchange stockExchange) {
        this.bids = bids;
        this.asks = asks;
        this.stockExchange = stockExchange;
    }

    /**
     * Executes the buy order. Matches the buy order against existing sell orders.
     * If a match is found, updates the orders and the stock exchange accordingly.
     *
     * @param params Parameters required for execution, including the order details.
     */
    @Override
    public void execute(Map<String, Object> params) {
        BuyOrder buyOrder = BuyOrder.createBuyOrderFromString((String)params.get("OrderJson"));
        bids.add(buyOrder);
        stockExchange.getTraderById(buyOrder.getTrader())
                .addOutstandingFunds(buyOrder.getAmount() * buyOrder.getPrice());

        String stock = buyOrder.getStockName();
        List<SellOrder> specific_orders = new ArrayList<>(
                this.asks.stream().filter(x -> x.getStockName().equals(stock) && !x.isResolved()
                        && !x.getTrader().equals(buyOrder.getTrader())).toList());
        specific_orders.sort(Comparator.comparingInt(SellOrder::getPrice));

        specific_orders.forEach(sellOrder -> {
            if(!buyOrder.isResolved()){
                sellOrder.handle(buyOrder, stockExchange);
            }
        });

        bids.removeIf(Order::isResolved);
        asks.removeIf(Order::isResolved);
    }
}
