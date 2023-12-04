package nl.rug.aoop.StockExchange;

import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.Traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestStockExchange {

    private StockExchange stockExchange;

    @BeforeEach
    public void setup() {
        Stock stock1 = new Stock("AAPL", "Apple Inc.", 1000000, 150.0);
        Stock stock2 = new Stock("GOOGL", "Alphabet Inc.", 750000, 2500.0);

        Trader trader1 = new Trader("T001", "Alice", 10000, null); // Assuming the trader has constructors like this.
        Trader trader2 = new Trader("T002", "Bob", 15000, null);

        List<Stock> stocks = Arrays.asList(stock1, stock2);
        List<Trader> traders = Arrays.asList(trader1, trader2);

        stockExchange = new StockExchange(stocks, traders);
    }


    @Test
    public void testGetStockBySymbol() {
        Stock result = stockExchange.getStockBySymbol("AAPL");
        assertNotNull(result);
        assertEquals("Apple Inc.", result.getName());

        result = stockExchange.getStockBySymbol("UNKNOWN");
        assertNull(result);
    }

    @Test
    public void testGetTraderById() {
        Trader result = stockExchange.getTraderById("T001");
        assertNotNull(result);
        assertEquals("Alice", result.getName());

        result = stockExchange.getTraderById("UNKNOWN");
        assertNull(result);
    }

    @Test
    public void testSerializationAndDeserialization() {
        String serialized = stockExchange.toJson();
        assertNotNull(serialized);

        StockExchange deserialized = StockExchange.createSEFromString(serialized);
        assertNotNull(deserialized);
        assertEquals(2, deserialized.getNumberOfStocks());
        assertEquals(2, deserialized.getNumberOfTraders());
    }


}
