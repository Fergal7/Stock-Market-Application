package nl.rug.aoop.Orders;

import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.Transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestBuyOrder {

    private BuyOrder buyOrder;
    private StockExchange mockStockExchange;
    private Order mockSellOrder;
    private Trader mockBuyer;
    private Trader mockSeller;
    private Stock mockStock;


    @BeforeEach
    public void setUp() {
        mockStockExchange = mock(StockExchange.class);
        mockSellOrder = mock(Order.class);
        mockBuyer = mock(Trader.class);
        mockSeller = mock(Trader.class);
        mockStock = mock(Stock.class);

        buyOrder = new BuyOrder("TestStock", 10, 100, "buyerId");

        when(mockStockExchange.getTraderById("buyerId")).thenReturn(mockBuyer);
        when(mockStockExchange.getTraderById("sellerId")).thenReturn(mockSeller); // Assuming "sellerId" is the ID for the seller

    }

    @Test
    public void testHandle() {
        when(mockSellOrder.getPrice()).thenReturn(90);
        when(mockSellOrder.getAmount()).thenReturn(5);
        when(mockSellOrder.getStockName()).thenReturn("TestStock");
        when(mockSellOrder.getTrader()).thenReturn("sellerId");
        when(mockStockExchange.getStockBySymbol(anyString())).thenReturn(mockStock);

        buyOrder.handle(mockSellOrder, mockStockExchange);

        verify(mockBuyer, times(1)).addTransaction(any(Transaction.class));
        verify(mockSeller, times(1)).addTransaction(any(Transaction.class));
    }

    @Test
    public void testSetAmountResolved() {
        buyOrder.setAmount(0);
        assertTrue(buyOrder.isResolved());
        assertEquals(0, buyOrder.getAmount());
    }

    @Test
    public void testSetAmountNotResolved() {
        buyOrder.setAmount(5);
        assertFalse(buyOrder.isResolved());
        assertEquals(5, buyOrder.getAmount());
    }

    @Test
    public void testToJsonAndFromJson() {
        String jsonString = buyOrder.toJson();
        BuyOrder recreatedBuyOrder = BuyOrder.createBuyOrderFromString(jsonString);

        assertEquals(buyOrder.getStockName(), recreatedBuyOrder.getStockName());
        assertEquals(buyOrder.getPrice(), recreatedBuyOrder.getPrice());
        assertEquals(buyOrder.getAmount(), recreatedBuyOrder.getAmount());
        assertEquals(buyOrder.getTrader(), recreatedBuyOrder.getTrader());
    }
}
