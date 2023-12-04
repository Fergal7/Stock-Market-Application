module command {
    requires static lombok;
    requires org.slf4j;
    // If you want to allow this module to be used in other modules, uncomment the following line:
    exports nl.rug.aoop.command;
    // Note that this will not include any sub-level packages. If you want to export more, then add those as well:
    //    exports exports nl.rug.aoop.command.example;
}