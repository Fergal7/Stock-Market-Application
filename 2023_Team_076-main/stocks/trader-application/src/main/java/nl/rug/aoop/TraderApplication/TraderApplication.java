package nl.rug.aoop.TraderApplication;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.MessageHandlers.TraderMessageHandler;
import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.TraderBot.TraderBot;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.TradingStrategies.RandomTradingStrategy;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messagequeue.NetworkProducer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main trader application that initializes trader bots and starts them.
 */
@Getter
@Setter
public class TraderApplication {

    /**
     * List of trader bots managed by this application.
     */
    private final List<TraderBot> traders;

    /**
     * List of stocks known to this application.
     */
    private final List<Stock> stocks;

    /**
     * The stock exchange associated with this trader application.
     */
    private StockExchange stockExchange;

    /**
     * Constructor for the TraderApplication.
     *
     * @param initTraders    Initial list of traders to be initialized as bots.
     * @param stockExchange  The stock exchange to be associated with the trader application.
     */
    public TraderApplication(List<Trader> initTraders, StockExchange stockExchange)  {
        this.stocks = null;
        this.stockExchange = stockExchange;
        traders = new ArrayList<>();

        if(!initTraders.isEmpty()) {
            initializeTraders(initTraders);
            startBots();
        }
    }

    /**
     * Starts all trader bots managed by this application.
     */
    private void startBots() {
        traders.forEach(Thread::start);
    }

    /**
     * Initializes traders by creating a TraderBot for each trader, setting up their network connections,
     * and adding them to the list of trader bots managed by this application.
     *
     * @param initTraders List of traders to be initialized.
     */
    private void initializeTraders(List<Trader> initTraders) {
        initTraders.forEach(trader -> {
            try {
                TraderBot traderBot = new TraderBot(trader, new RandomTradingStrategy(stockExchange), null);
                InetSocketAddress address = new InetSocketAddress("localhost", getPortFromEnvOrDefault());
                Client client = new Client(address, new TraderMessageHandler(traderBot, this), trader.getId());
                client.initSocket();
                client.start();

                NetworkProducer networkProducer = new NetworkProducer(client);
                traderBot.setNetworkProducer(networkProducer);
                traders.add(traderBot);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Returns the port number from the environment or a default value if not specified in the environment.
     *
     * @return Port number.
     */
    private int getPortFromEnvOrDefault() {
        int serverPort;
        try {
            serverPort = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (NumberFormatException e) {
            serverPort = 6000;
        }
        return serverPort;
    }
}
