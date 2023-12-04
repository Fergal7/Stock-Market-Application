package nl.rug.aoop.MessageHandlers;

import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.TraderApplication.TraderApplication;
import nl.rug.aoop.TraderBot.TraderBot;
import nl.rug.aoop.Traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestTraderMessageHandler {

    private TraderBot mockTraderBot;
    private TraderApplication mockTraderApplication;
    private TraderMessageHandler handler;

    @BeforeEach
    public void setup() {
        mockTraderBot = Mockito.mock(TraderBot.class);
        mockTraderApplication = Mockito.mock(TraderApplication.class);
        handler = new TraderMessageHandler(mockTraderBot, mockTraderApplication);
    }

    @Test
    public void testHandleMessageStockUpdate() {
        String testMessage = "{\"header\":\"StockUpdate\", \"body\":\"someBodyContent\"}";
        StockExchange mockStockExchange = Mockito.mock(StockExchange.class);

        try (MockedStatic<StockExchange> mocked = Mockito.mockStatic(StockExchange.class)) {
            mocked.when(() -> StockExchange.createSEFromString(anyString())).thenReturn(mockStockExchange);
            handler.handleMessage(testMessage);
            verify(mockTraderApplication, times(1)).setStockExchange(mockStockExchange);
        }
    }

    @Test
    public void testHandleMessageTraderUpdate() {
        String testMessage = "{\"header\":\"TraderUpdate\", \"body\":\"someBodyContent\"}";
        Trader mockTrader = Mockito.mock(Trader.class);

        try (MockedStatic<Trader> mocked = Mockito.mockStatic(Trader.class)) {
            mocked.when(() -> Trader.createTraderFromString(anyString())).thenReturn(mockTrader);
            handler.handleMessage(testMessage);
            verify(mockTraderBot, times(1)).setTrader(mockTrader);
        }
    }

}
