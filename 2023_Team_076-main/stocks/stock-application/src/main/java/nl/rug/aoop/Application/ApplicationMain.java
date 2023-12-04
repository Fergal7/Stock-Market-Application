package nl.rug.aoop.Application;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.Stock.Stock;
import nl.rug.aoop.StockExchange.StockExchange;
import nl.rug.aoop.Stock.StockList;
import nl.rug.aoop.Traders.Trader;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * The main class for the application responsible for initializing the stock exchange,
 * loading stock and trader data and starting the application.
 */
@Slf4j
public class ApplicationMain {

    /**
     * The main entry point for the application.
     * Loads stocks and traders data, initializes the stock exchange and starts the application.
     *
     * @param args Command line
     * @throws IOException If there is an issue reading the stocks or traders data files.
     */
    public static void main(String[] args) throws IOException {
        List<Stock> stocks = loadStocks();
        List<Trader> traders = loadTraders();

        StockExchange se = new StockExchange(stocks, traders);
        SimpleViewFactory view = new SimpleViewFactory();
        Application app = new Application(se, view);
        app.start();
    }

    /**
     * Loads traders data from the YAML file and returns a list of Trader objects.
     *
     * @return A list of Trader objects based on the data from the YAML file.
     * @throws IOException If there is an issue reading the traders data file.
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
     * Loads stocks data from the YAML file and returns a list of Stock objects.
     *
     * @return A list of Stock objects based on the data from the YAML file.
     * @throws IOException If there is an issue reading the stocks data file.
     */
    private static List<Stock> loadStocks() throws IOException {
        YamlLoader loader = new YamlLoader(Paths.get("data\\stocks.yaml"));
        StockList stockList = loader.load(StockList.class);
        return stockList.getStocks();
    }
}
