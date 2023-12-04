module stock.application {
    requires static lombok;
    requires org.slf4j;
    requires messagequeue;
    requires networking;
    requires command;
    requires stock.market.ui;
    requires util;
    requires com.fasterxml.jackson.annotation;
    requires com.google.gson;
    requires org.mockito;

    opens nl.rug.aoop.Orders to com.google.gson;
    opens nl.rug.aoop.Transaction to com.google.gson;

    opens nl.rug.aoop.Stock;
    opens nl.rug.aoop.Traders;
    opens nl.rug.aoop.StockExchange;
    exports nl.rug.aoop.Traders;
    exports nl.rug.aoop.Orders;
    exports nl.rug.aoop.Stock;
    exports nl.rug.aoop.StockExchange;
    // If you want to allow this module to be used in other modules, uncomment the following line:

    // Note that this will not include any sub-level packages. If you want to export more, then add those as well:
    //    exports exports nl.rug.aoop.command.example;
}