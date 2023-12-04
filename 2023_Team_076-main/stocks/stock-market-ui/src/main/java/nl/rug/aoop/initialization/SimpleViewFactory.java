package nl.rug.aoop.initialization;

import com.formdev.flatlaf.FlatDarculaLaf;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.simpleview.TerminalFrame;

import javax.swing.*;

/**
 * Creates a new SWING UI for the provided stock exchange.
 */
public class SimpleViewFactory implements AbstractViewFactory {
    @Override
    public void createView(StockExchangeDataModel stockExchangeDataModel) {
        FlatDarculaLaf.setup();
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> {
            new TerminalFrame(stockExchangeDataModel);
        });
    }
}
