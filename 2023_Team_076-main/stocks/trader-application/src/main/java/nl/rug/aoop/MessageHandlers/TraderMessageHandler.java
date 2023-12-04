package nl.rug.aoop.MessageHandlers;

import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.TraderApplication.TraderApplication;
import nl.rug.aoop.TraderBot.TraderBot;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.networking.messages.MessageHandler;

/**
 * Handles messages specifically for traders, updating the state of the trader and stock exchange
 * based on the messages received.
 */
public class TraderMessageHandler implements MessageHandler<Void> {

    /** The trader bot associated with this handler. */
    private final TraderBot traderBot;

    /** The trader application associated with this handler. */
    private final TraderApplication traderApplication;

    /**
     * Constructs a new TraderMessageHandler with the provided trader bot and application.
     *
     * @param traderBot The trader bot to be associated with this handler.
     * @param traderApplication The trader application to be associated with this handler.
     */
    public TraderMessageHandler(TraderBot traderBot, TraderApplication traderApplication) {
        this.traderBot = traderBot;
        this.traderApplication = traderApplication;
    }

    /**
     * Handles incoming messages, updating the state of the trader and stock exchange accordingly.
     *
     * @param message The incoming message to be handled.
     * @return null
     */
    @Override
    public Void handleMessage(String message) {
        Message serverMessage = Message.createMessageFromString(message);

        if (serverMessage.getHeader().equals("StockUpdate")){
            StockExchange stockExchange = StockExchange.createSEFromString(serverMessage.getBody());
            this.traderApplication.setStockExchange(stockExchange);
        }

        if (serverMessage.getHeader().equals("TraderUpdate")){
            Trader trader = Trader.createTraderFromString(serverMessage.getBody());
            traderBot.setTrader(trader);
            System.out.println(trader.getSpendableFunds());
        }

        return null;
    }

}
