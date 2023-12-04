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
public class TraderTerminalTable extends JTable {

    /**
     * Header of the column containing the trader names.
     */
    public static final String COL_NAME = "Name";
    /**
     * Header of the column containing the funds.
     */
    public static final String COL_FUNDS = "Funds";
    /**
     * Header of the column containing the owned stocks.
     */
    public static final String COL_OWNED_STOCKS = "Owned Stocks";
    /**
     * Total number of columns to be displayed in the table.
     */
    public static final int NUM_COLS = 3;

    /**
     * Creates a new table that displays the different stocks and their info.
     *
     * @param stockTableModel The table model that backs this table.
     */
    public TraderTerminalTable(AbstractTableModel stockTableModel) {
        super(stockTableModel);
        setColumnAlignment(DefaultTableCellRenderer.LEFT, Arrays.asList(COL_NAME));
        setColumnAlignment(DefaultTableCellRenderer.CENTER, Arrays.asList(COL_FUNDS));
        setColumnAlignment(DefaultTableCellRenderer.RIGHT, Arrays.asList(COL_OWNED_STOCKS));
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
