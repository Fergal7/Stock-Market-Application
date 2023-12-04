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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLimitBuyCommand {
    private StockExchange mockStockExchange;
    private Trader mockTrader;
    private LimitBuyCommand command;
    private ArrayList<BuyOrder> bids;

    @BeforeEach
    void setUp() {
        mockStockExchange = mock(StockExchange.class);
        mockTrader = mock(Trader.class);
        bids = new ArrayList<>();
        ArrayList<SellOrder> asks = new ArrayList<>();
        command = new LimitBuyCommand(bids, asks, mockStockExchange);
    }

    @Test
    void testExecute() {
        String buyOrderJson = "{\"stockName\":\"Apple\",\"amount\":10,\"price\":150,\"trader\":\"trader1\"}";
        BuyOrder expectedBuyOrder = BuyOrder.createBuyOrderFromString(buyOrderJson);

        when(mockStockExchange.getTraderById("trader1")).thenReturn(mockTrader);

        Map<String, Object> params = new HashMap<>();
        params.put("OrderJson", buyOrderJson);

        command.execute(params);

        // Ensure the bids list has exactly one item (the BuyOrder we expect)
        assertEquals(1, bids.size());

        // Compare the actual BuyOrder in the bids list to our expected BuyOrder
        BuyOrder actualBuyOrder = bids.get(0);
        assertEquals(expectedBuyOrder.getAmount(), actualBuyOrder.getAmount());
        assertEquals(expectedBuyOrder.getPrice(), actualBuyOrder.getPrice());
        assertEquals(expectedBuyOrder.getTrader(), actualBuyOrder.getTrader());

        verify(mockTrader).addOutstandingFunds(1500); // 10 * 150
    }

}
