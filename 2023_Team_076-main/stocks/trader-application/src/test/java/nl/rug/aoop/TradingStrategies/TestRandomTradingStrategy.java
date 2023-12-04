package nl.rug.aoop.TradingStrategies;

import nl.rug.aoop.Orders.BuyOrder;
import nl.rug.aoop.Orders.Order;
import nl.rug.aoop.Orders.SellOrder;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.Traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestRandomTradingStrategy {

    private StockExchange mockExchange;
    private RandomTradingStrategy strategy;
    private Trader trader;

    @BeforeEach
    public void setup() {
        mockExchange = Mockito.mock(StockExchange.class);
        when(mockExchange.getStockBySymbol("AAPL"))
                .thenReturn(new Stock("AAPL", "Apple", 100, 100));
        strategy = new RandomTradingStrategy(mockExchange);

        trader = new Trader("1", "Trader1", 1000, new HashMap<>());
    }

    @Test
    public void testConstuctor() {
        strategy = new RandomTradingStrategy(mockExchange);
        assertNotNull(strategy);
    }

    @Test
    public void testGenerateBuyOrder() {
        when(mockExchange.getStocks()).thenReturn(Collections.singletonList(new Stock("AAPL", "Apple", 100, 100)));
        Order order = strategy.generateOrder(trader);
        while (order != null && order.getClass() != BuyOrder.class ){
            order = strategy.generateOrder(trader);
        }

        assertTrue(order == null || order.getPrice() <= 105);
    }

    @Test
    public void testGenerateSellOrder() {
        trader.getOwnedStocksMap().put("AAPL", 10);
        when(mockExchange.getStocks()).thenReturn(Collections.singletonList(new Stock("AAPL", "Apple", 100, 100)));
        Order order = strategy.generateOrder(trader);
        while (order != null && order.getClass() != SellOrder.class ){
            order = strategy.generateOrder(trader);
        }
        assertTrue(order == null || order.getPrice() >= 95);
    }


    @Test
    public void testInsufficientFundsForBuy() {
        trader.setFunds(50);
        when(mockExchange.getStocks()).thenReturn(Collections.singletonList(new Stock("AAPL", "Apple", 100, 100)));
        Order order = strategy.generateOrder(trader);
        assertTrue(order instanceof SellOrder || order == null);
    }

    @Test
    public void testInsufficientStocksForSell() {
        trader.getOwnedStocksMap().put("AAPL", 1);
        when(mockExchange.getStocks()).thenReturn(Collections.singletonList(new Stock("AAPL", "Apple", 100, 100)));
        Order order = strategy.generateOrder(trader);
        assertTrue(order instanceof BuyOrder || order == null);
    }
}
