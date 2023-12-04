package nl.rug.aoop.Orders;

import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.Transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestSellOrder {

    private SellOrder sellOrder;
    private StockExchange mockStockExchange;
    private Order mockBuyOrder;
    private Trader mockSeller;
    private Trader mockBuyer;
    private Stock mockStock;


    @BeforeEach
    public void setUp() {
        mockStockExchange = mock(StockExchange.class);
        mockBuyOrder = mock(Order.class);
        mockSeller = mock(Trader.class);
        mockBuyer = mock(Trader.class);
        mockStock = mock(Stock.class);


        sellOrder = new SellOrder("TestStock", 10, 100, "sellerId");

        when(mockStockExchange.getTraderById("sellerId")).thenReturn(mockSeller);
        when(mockStockExchange.getTraderById("buyerId")).thenReturn(mockBuyer);

    }

    @Test
    public void testHandle() {
        when(mockBuyOrder.getPrice()).thenReturn(110);
        when(mockBuyOrder.getAmount()).thenReturn(5);
        when(mockBuyOrder.getStockName()).thenReturn("TestStock");
        when(mockBuyOrder.getTrader()).thenReturn("buyerId");
        when(mockStockExchange.getStockBySymbol(anyString())).thenReturn(mockStock);


        sellOrder.handle(mockBuyOrder, mockStockExchange);

        verify(mockSeller, times(1)).addTransaction(any(Transaction.class));
        verify(mockBuyer, times(1)).addTransaction(any(Transaction.class));
    }

    @Test
    public void testSetAmountResolved() {
        sellOrder.setAmount(0);
        assertTrue(sellOrder.isResolved());
        assertEquals(0, sellOrder.getAmount());
    }

    @Test
    public void testSetAmountNotResolved() {
        sellOrder.setAmount(5);
        assertFalse(sellOrder.isResolved());
        assertEquals(5, sellOrder.getAmount());
    }

    @Test
    public void testToJsonAndFromJson() {
        String jsonString = sellOrder.toJson();
        SellOrder recreatedSellOrder = SellOrder.createMessageFromString(jsonString);

        assertEquals(sellOrder.getStockName(), recreatedSellOrder.getStockName());
        assertEquals(sellOrder.getPrice(), recreatedSellOrder.getPrice());
        assertEquals(sellOrder.getAmount(), recreatedSellOrder.getAmount());
        assertEquals(sellOrder.getTrader(), recreatedSellOrder.getTrader());
    }
}
