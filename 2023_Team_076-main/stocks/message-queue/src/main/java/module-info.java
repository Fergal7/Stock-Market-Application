module messagequeue {
    requires static lombok;
    requires com.google.gson;
    opens nl.rug.aoop.messagequeue to com.google.gson;
    exports nl.rug.aoop.producer;
    exports nl.rug.aoop.consumer;
    exports nl.rug.aoop.messagequeue;
}