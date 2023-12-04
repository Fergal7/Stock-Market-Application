package nl.rug.aoop.Traders;

import nl.rug.aoop.Transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTrader {

    private Trader trader;

    @BeforeEach
    void setUp() {
        Map<String, Integer> initialStocks = new HashMap<>();
        initialStocks.put("AAPL", 100);
        trader = new Trader("1", "John", 10000.0, initialStocks);
    }

    @Test
    void testAddAndRemoveOutstandingFunds() {
        trader.addOutstandingFunds(200.0);
        assertEquals(9800.0, trader.getSpendableFunds());

        trader.removeOutstandingFunds(100.0);
        assertEquals(9900.0, trader.getSpendableFunds());
    }

    @Test
    void testAddAndRemoveOutstandingStocks() {
        trader.addOutstandingStocks("AAPL", 20);
        assertEquals(80, trader.getSpendableStocks("AAPL"));

        trader.removeOutstandingStock("AAPL", 10);
        assertEquals(90, trader.getSpendableStocks("AAPL"));
    }

    @Test
    void testAddTransactionBuy() {
        Transaction buyTransaction = new Transaction("Buy", "AAPL", 50, 100);
        trader.addTransaction(buyTransaction);

        assertEquals(5000.0, trader.getFunds());
        assertEquals(150, trader.getOwnedStocksMap().get("AAPL").intValue());
    }

    @Test
    void testAddTransactionSell() {
        Transaction sellTransaction = new Transaction("Sell", "AAPL", 50, 100);
        trader.addTransaction(sellTransaction);

        assertEquals(15000.0, trader.getFunds());  // Correcting the expected value
        assertEquals(50, trader.getOwnedStocksMap().get("AAPL").intValue());
    }


}
