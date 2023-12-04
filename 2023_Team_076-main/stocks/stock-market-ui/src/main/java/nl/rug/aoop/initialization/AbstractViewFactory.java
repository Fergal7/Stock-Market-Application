package nl.rug.aoop.initialization;

import nl.rug.aoop.model.StockExchangeDataModel;

/**
 * Factory for creating views of a stock exchange data model.
 */
public interface AbstractViewFactory {

    /**
     * Starts up a new GUI for the provided data model.
     *
     * @param stockExchangeDataModel Data model of a stock exchange.
     */
    void createView(StockExchangeDataModel stockExchangeDataModel);
}
