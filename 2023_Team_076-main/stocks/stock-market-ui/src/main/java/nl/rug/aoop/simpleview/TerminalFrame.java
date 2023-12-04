package nl.rug.aoop.simpleview;

import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.simpleview.tables.StockTableModel;
import nl.rug.aoop.simpleview.tables.StockTerminalTable;
import nl.rug.aoop.simpleview.tables.TraderTableModel;
import nl.rug.aoop.simpleview.tables.TraderTerminalTable;

import javax.swing.*;
import java.awt.*;

/**
 * A custom frame for the view of the stocks.
 */
public class TerminalFrame extends JFrame {

    /**
     * The width of the frame.
     */
    public static final int WIDTH = 800;
    /**
     * The height of the frame.
     */
    public static final int HEIGHT = 800;

    /**
     * Creates a new frame for the stock terminal view.
     *
     * @param stockExchange A data model of the stock exchange.
     */
    public TerminalFrame(StockExchangeDataModel stockExchange) {
        super("Stonks Terminal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JTable stockTable = new StockTerminalTable(new StockTableModel(stockExchange));
        JScrollPane stockTableScrollPane = new JScrollPane(stockTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JTable traderTable = new TraderTerminalTable(new TraderTableModel(stockExchange));
        JScrollPane traderTableScrollPane = new JScrollPane(traderTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JSplitPane topBottomSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                stockTableScrollPane,
                traderTableScrollPane);
        topBottomSplit.setDividerLocation(HEIGHT / 2);
        add(topBottomSplit);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
