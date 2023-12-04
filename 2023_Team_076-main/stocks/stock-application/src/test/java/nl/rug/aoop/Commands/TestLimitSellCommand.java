package nl.rug.aoop.Commands;

import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.SellOrder;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestLimitSellCommand {
    private StockExchange mockStockExchange;
    private Trader mockTrader;
    private LimitSellCommand command;
    private ArrayList<SellOrder> asks;

    @BeforeEach
    void setUp() {
        mockStockExchange = mock(StockExchange.class);
        mockTrader = mock(Trader.class);
        asks = new ArrayList<>();
        ArrayList<BuyOrder> bids = new ArrayList<>();
        command = new LimitSellCommand(bids, asks, mockStockExchange);
    }

    @Test
    void testExecute() {
        String sellOrderJson = "{\"stockName\":\"Apple\",\"amount\":10,\"price\":150,\"trader\":\"trader1\"}";
        SellOrder expectedSellOrder = SellOrder.createMessageFromString(sellOrderJson);

        when(mockStockExchange.getTraderById("trader1")).thenReturn(mockTrader);

        Map<String, Object> params = new HashMap<>();
        params.put("OrderJson", sellOrderJson);

        command.execute(params);

        assertEquals(1, asks.size());

        SellOrder actualSellOrder = asks.get(0);

        assertEquals(expectedSellOrder.getStockName(), actualSellOrder.getStockName());
        assertEquals(expectedSellOrder.getAmount(), actualSellOrder.getAmount());
        assertEquals(expectedSellOrder.getPrice(), actualSellOrder.getPrice());
        assertEquals(expectedSellOrder.getTrader(), actualSellOrder.getTrader());

        verify(mockTrader).addOutstandingStocks("Apple", 10);
        verify(mockStockExchange).getTraderById("trader1");
    }

}
