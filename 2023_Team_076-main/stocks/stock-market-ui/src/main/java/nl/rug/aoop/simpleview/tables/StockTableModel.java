package nl.rug.aoop.simpleview.tables;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.simpleview.NumberFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Table model for the stock data.
 */
@Slf4j
public class StockTableModel extends AbstractTableModel {

    private final StockExchangeDataModel stockExchange;

    /**
     * Creates a new table that can display stock information.
     *
     * @param stockExchange The stock exchange data representation containing the stocks to be displayed in the table.
     */
    public StockTableModel(StockExchangeDataModel stockExchange) {
        this.stockExchange = stockExchange;
        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(() ->
                        SwingUtilities.invokeLater(this::fireTableDataChanged),
                0, 1, TimeUnit.SECONDS);
    }

    @Override
    public int getRowCount() {
        return stockExchange.getNumberOfStocks();
    }

    @Override
    public int getColumnCount() {
        return StockTerminalTable.NUM_COLS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StockDataModel stock = stockExchange.getStockByIndex(rowIndex);
        if (stock == null) {
            return null;
        }
        return switch (columnIndex) {
            case 0 -> stock.getSymbol();
            case 1 -> stock.getName();
            case 2 -> NumberFormatter.largeNumberFormat(stock.getSharesOutstanding());
            case 3 -> NumberFormatter.largeNumberFormat(stock.getMarketCap());
            case 4 -> NumberFormatter.largeNumberFormat(stock.getPrice());
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> StockTerminalTable.COL_SYMBOL;
            case 1 -> StockTerminalTable.COL_NAME;
            case 2 -> StockTerminalTable.COL_SHARES_OUTSTANDING;
            case 3 -> StockTerminalTable.COL_MARKET_CAP;
            case 4 -> StockTerminalTable.COL_PRICE;
            default -> null;
        };
    }
}
