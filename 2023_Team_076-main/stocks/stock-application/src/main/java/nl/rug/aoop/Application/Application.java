package nl.rug.aoop.Application;

import nl.rug.aoop.Commands.LimitBuyCommand;
import nl.rug.aoop.Commands.LimitSellCommand;
import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.SellOrder;
import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;
import nl.rug.aoop.networking.messagequeue.MqPutCommand;
import nl.rug.aoop.networking.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.networking.messages.OurMessageHandler;
import nl.rug.aoop.networking.server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the stock trading application, handling communications, updates and message processing.
 */
public class Application extends Thread {

    private final MessageQueue messageQueue;
    private Server server;
    private final StockExchange stockExchange;

    private final CommandHandler stockCommandHandler;

    /**
     * Initializes the application with a given stock exchange and view.
     *
     * @param stockExchange The stock exchange system.
     * @param viewFactory   View factory for the stock exchange.
     * @throws IOException If communication setup fails.
     */
    public Application(StockExchange stockExchange, SimpleViewFactory viewFactory) throws IOException {
        this.messageQueue = new NetworkOrderedQueue();
        this.stockExchange = stockExchange;
        this.stockCommandHandler = new CommandHandler();

        ArrayList<BuyOrder> bids = new ArrayList<>();
        ArrayList<SellOrder> asks = new ArrayList<>();

        stockCommandHandler.registerCommand("Buy", new LimitBuyCommand(bids, asks, stockExchange));
        stockCommandHandler.registerCommand("Sell", new LimitSellCommand(bids, asks,stockExchange));

        viewFactory.createView(stockExchange);
        initCommunication();
    }

    /**
     * Sets up communication channels and server.
     *
     * @throws IOException If server setup fails.
     */
    public void initCommunication() throws IOException {
        int serverPort;
        try {
            serverPort = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (NumberFormatException e) {
            serverPort = 6000;
        }

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.registerCommand("MqPut", new MqPutCommand(this.messageQueue));

        OurMessageHandler messageHandler = new OurMessageHandler(commandHandler);

        this.server = new Server(serverPort, messageHandler);
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(server);
    }

    /**
     * Sends updates to all clients about stocks and traders.
     */
    private void updateClients() {
        StringBuilder stocks = new StringBuilder();
        for (Stock stock : this.stockExchange.getStocks()) {
            stocks.append(stock.getSymbol()).append(":")
                    .append(stock.getPrice()).append(":").append(stock.getSharesOutstanding()).append("-");
        }
        Message stockMessage = new Message("StockUpdate", this.stockExchange.toJson());

        this.server.serverSendMessageToEveryClient(stockMessage.toJson());

        this.stockExchange.getTraders().forEach(trader -> {
            String id = trader.getId();
            String test = trader.toJson();
            Message traderMessage = new Message("TraderUpdate", test);
            this.server.serverSendMessageToSpecificClient(traderMessage.toJson(), id);
        });
    }

    /**
     * Processes any new messages from the queue.
     */
    private void pollMessageQueue() {
        if (this.messageQueue.getSize() != 0) {
            Message message = this.messageQueue.dequeue();

            Map<String, Object> params = new HashMap<>();

            params.put("OrderJson", message.getBody());

            stockCommandHandler.executeCommand(message.getHeader(), params);
        }
    }

    /**
     * Application main loop: updates clients, processes messages and waits.
     */
    @Override
    public void run() {
        int WAIT_TIME = 100;
        int updatedClientsMilliSecondsAgo = 0;

        while (true) {
            try {
                if (updatedClientsMilliSecondsAgo > 1000) {
                    updateClients();
                    updatedClientsMilliSecondsAgo = 0;
                }

                pollMessageQueue();

                updatedClientsMilliSecondsAgo += WAIT_TIME;
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
