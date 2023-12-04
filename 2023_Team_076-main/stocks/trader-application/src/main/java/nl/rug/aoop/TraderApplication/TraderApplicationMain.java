package nl.rug.aoop.TraderApplication;

import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main class for the Trader Application responsible for initializing traders, stocks and
 * launching the trading application.
 */
public class TraderApplicationMain {

    /**
     * The entry point for the application.
     *
     * @param args The command line arguments.
     * @throws IOException If there's an error reading.
     */
    public static void main(String[] args) throws IOException {
        List<Trader> traders = loadTraders();
        List<Stock> stocks = loadStocks();

        StockExchange stockExchange = new StockExchange(stocks, traders);
        TraderApplication traderApp = new TraderApplication(traders, stockExchange);
    }

    /**
     * Loads trader information from a YAML file and initializes a list of Trader objects.
     *
     * @return A list of Trader objects based on the configuration file.
     * @throws IOException If there's an error reading the trader configuration file.
     */
    @SuppressWarnings("unchecked")
    private static List<Trader> loadTraders() throws IOException {
        YamlLoader loader = new YamlLoader(Paths.get("data\\traders.yaml"));
        List<Map<String, Object>> rawData = loader.load(List.class);

        List<Trader> traders = new ArrayList<>();
        for (Map<String, Object> entry : rawData) {
            String id = (String) entry.get("id");
            String name = (String) entry.get("name");
            double funds = Double.parseDouble(entry.get("funds").toString());
            Map<String, Integer> ownedStocks =
                    (Map<String, Integer>) ((Map<String, Object>) entry.get("stockPortfolio")).get("ownedShares");
            traders.add(new Trader(id, name, funds, ownedStocks));
        }
        return traders;
    }

    /**
     * Loads stock information from a YAML file and initializes a list of Stock objects.
     *
     * @return A list of Stock objects based on the configuration file.
     * @throws IOException If there's an error reading the stock configuration file.
     */
    @SuppressWarnings("unchecked")
    private static List<Stock> loadStocks() throws IOException {
        YamlLoader loader = new YamlLoader(Paths.get("data\\stocks.yaml"));

        // Load the entire file into a Map
        Map<String, Map<String, Map<String, Object>>> dataMap = loader.load(Map.class);

        // Extract the stocks map from the dataMap using the "stocks" key
        Map<String, Map<String, Object>> stocksMap = dataMap.get("stocks");

        List<Stock> stocks = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : stocksMap.entrySet()) {
            Map<String, Object> stockData = entry.getValue();
            String symbol = (String) stockData.get("symbol");
            String name = (String) stockData.get("name");
            long sharesOutstanding = Long.parseLong(stockData.get("sharesOutstanding").toString());
            double price = Double.parseDouble(stockData.get("initialPrice").toString());
            stocks.add(new Stock(symbol, name, sharesOutstanding, price));
        }
        return stocks;
    }
}
