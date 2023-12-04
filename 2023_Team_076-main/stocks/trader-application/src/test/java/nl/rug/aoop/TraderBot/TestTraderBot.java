package nl.rug.aoop.TraderBot;

import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.TradingStrategies.TradingStrategy;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.networking.messagequeue.NetworkProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestTraderBot {

    private Trader trader;
    private TradingStrategy strategy;
    private NetworkProducer networkProducer;
    private TraderBot traderBot;

    @BeforeEach
    public void setUp() {
        trader = mock(Trader.class);
        strategy = mock(TradingStrategy.class);
        networkProducer = mock(NetworkProducer.class);

        traderBot = new TraderBot(trader, strategy, networkProducer);
    }

    @Test
    public void testRunSendsBuyOrderMessage() throws InterruptedException {
        // Mock the TradingStrategy to return a BuyOrder
        BuyOrder mockOrder = mock(BuyOrder.class);
        when(mockOrder.toJson()).thenReturn("mockBuyOrderJson");
        when(strategy.generateOrder(trader)).thenReturn(mockOrder);

        // Start the TraderBot in a separate thread for a short duration
        Thread botThread = new Thread(traderBot);
        botThread.start();
        Thread.sleep(2000);  // Allow the bot to run for 2 seconds
        botThread.interrupt();  // Stop the bot

        // Verify that the NetworkProducer's put method was called with the correct Message
        verify(networkProducer, atLeastOnce()).put(any(Message.class));
    }

}
