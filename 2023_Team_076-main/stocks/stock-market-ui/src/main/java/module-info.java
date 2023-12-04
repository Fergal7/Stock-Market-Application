module stock.market.ui {
    requires static lombok;
    exports nl.rug.aoop.model;
    exports nl.rug.aoop.initialization;
    requires org.slf4j;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires java.net.http;
    requires jdk.httpserver;
    requires com.google.gson;
    opens nl.rug.aoop.webview.data to com.google.gson;

}