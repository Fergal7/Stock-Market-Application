package nl.rug.aoop.simpleview.tables;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Table component for the different stock info.
 *
 * @see StockTableModel
 */
public class StockTerminalTable extends JTable {

    /**
     * Header of the column containing the stock symbols.
     */
    public static final String COL_SYMBOL = "Symbol";
    /**
     * Header of the column containing the company names.
     */
    public static final String COL_NAME = "Name";
    /**
     * Header of the column containing the shares outstanding.
     */
    public static final String COL_SHARES_OUTSTANDING = "Shares Outstanding";
    /**
     * Header of the column containing the market cap.
     */
    public static final String COL_MARKET_CAP = "Market Cap";
    /**
     * Header of the column containing the stock price.
     */
    public static final String COL_PRICE = "Price";
    /**
     * Total number of columns to be displayed in the table.
     */
    public static final int NUM_COLS = 5;

    /**
     * Creates a new table that displays the different stocks and their info.
     *
     * @param stockTableModel The table model that backs this table.
     */
    public StockTerminalTable(AbstractTableModel stockTableModel) {
        super(stockTableModel);
        setColumnAlignment(DefaultTableCellRenderer.LEFT, Arrays.asList(COL_NAME, COL_SYMBOL));
        setColumnAlignment(DefaultTableCellRenderer.CENTER, Arrays.asList(COL_SHARES_OUTSTANDING, COL_MARKET_CAP));
        setColumnAlignment(DefaultTableCellRenderer.RIGHT, Arrays.asList(COL_PRICE));
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Method that sets the alignment of a given column.
     *
     * @param alignment The alignment the columns should have.
     * @param columns   The names of the columns that should be aligned.
     */
    private void setColumnAlignment(int alignment, Collection<String> columns) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        columns.forEach(c -> this.getColumn(c).setCellRenderer(renderer));
    }
}
