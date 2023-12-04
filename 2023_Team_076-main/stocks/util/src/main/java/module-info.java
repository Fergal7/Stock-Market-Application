module util {
    requires static lombok;
    exports nl.rug.aoop.util;
    requires org.slf4j;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
}