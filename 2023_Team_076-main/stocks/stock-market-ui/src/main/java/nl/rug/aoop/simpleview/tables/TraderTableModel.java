package nl.rug.aoop.simpleview.tables;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.simpleview.NumberFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Table model for the trader data.
 */
@Slf4j
public class TraderTableModel extends AbstractTableModel {

    private final StockExchangeDataModel stockExchange;

    /**
     * Creates a new table that can display trader information.
     *
     * @param stockExchange The stock exchange data representation containing the traders to be displayed in the table.
     */
    public TraderTableModel(StockExchangeDataModel stockExchange) {
        this.stockExchange = stockExchange;
        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(() -> SwingUtilities.invokeLater(this::fireTableDataChanged), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public int getRowCount() {
        return stockExchange.getNumberOfTraders();
    }

    @Override
    public int getColumnCount() {
        return TraderTerminalTable.NUM_COLS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TraderDataModel trader = stockExchange.getTraderByIndex(rowIndex);
        if (trader == null) {
            return null;
        }
        return switch (columnIndex) {
            case 0 -> trader.getName();
            case 1 -> NumberFormatter.largeNumberFormat(trader.getFunds());
            case 2 -> trader.getOwnedStocks();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> TraderTerminalTable.COL_NAME;
            case 1 -> TraderTerminalTable.COL_FUNDS;
            case 2 -> TraderTerminalTable.COL_OWNED_STOCKS;
            default -> null;
        };
    }
}
