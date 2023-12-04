package nl.rug.aoop.Stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStockList {

    private StockList stockList;

    @BeforeEach
    void setUp() {
        stockList = new StockList();
    }

    @Test
    void testGetStocks() {
        Map<String, Stock> stocksMap = new LinkedHashMap<>();
        stocksMap.put("AAPL", new Stock("AAPL", "Apple", 1000L, 150.0));
        stocksMap.put("GOOGL", new Stock("GOOGL", "Google", 800L, 2500.0));

        stockList.setStocks(stocksMap);

        List<Stock> retrievedStocks = stockList.getStocks();

        assertEquals(2, retrievedStocks.size());
        assertEquals("Apple", retrievedStocks.get(0).getName());
        assertEquals("Google", retrievedStocks.get(1).getName());
    }

}
