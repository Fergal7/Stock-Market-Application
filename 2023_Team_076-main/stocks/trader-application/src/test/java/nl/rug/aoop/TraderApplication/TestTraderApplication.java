package nl.rug.aoop.TraderApplication;

import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestTraderApplication {

    private StockExchange mockStockExchange;
    private Trader mockTrader;
    private List<Trader> mockTraderList;
    private TraderApplication traderApplication;


    @Test
    public void testConstructorEmpty() throws IOException {
        ArrayList<Trader> t=  new ArrayList<>();
        TraderApplication traderApplication1 = new TraderApplication(t, mockStockExchange);
        assertNotNull(traderApplication1);
    }

    @Test
    public void testConstructorTryingToConnect() throws IOException {
        // Trying to connect to server but server not up.
        mockStockExchange = mock(StockExchange.class);
        mockTrader = mock(Trader.class);
        mockTraderList = Arrays.asList(mockTrader);
        assertThrows(RuntimeException.class, () -> {
            TraderApplication t = new TraderApplication(mockTraderList, mockStockExchange);
        });
    }


}