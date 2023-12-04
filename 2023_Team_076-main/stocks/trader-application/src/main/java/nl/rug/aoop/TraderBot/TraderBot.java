package nl.rug.aoop.TraderBot;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.TradingStrategies.TradingStrategy;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.networking.messagequeue.NetworkProducer;

import java.util.Random;

/**
 * Trader bot that sends buy and sell offer to the stock application.
 */
@Getter
@Setter
public class TraderBot extends Thread {
    private Trader trader;
    private NetworkProducer networkProducer;
    private TradingStrategy tradingStrategy;

    /**
     * Constructor of the trader bot, with all parameters.
     * @param trader the trader that is associated with the bot
     * @param strategy the trading strategy the TradingBot should follow
     * @param networkProducer network-producer the TradingBot uses to communication of the network
     */
    public TraderBot(Trader trader, TradingStrategy strategy, NetworkProducer networkProducer) {
        this.trader = trader;
        this.networkProducer = networkProducer;
        this.tradingStrategy = strategy;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = tradingStrategy.generateOrder(trader);
                if (order != null) {
                    String orderJson = order.toJson();
                    Message orderMessage;
                    if (order.getClass().equals(BuyOrder.class)) {
                        orderMessage = new Message("Buy", orderJson);
                    } else {
                        orderMessage = new Message("Sell", orderJson);
                    }

                    networkProducer.put(orderMessage);
                }

                int sleepTime = 1000 * (1 + new Random().nextInt(4));
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}