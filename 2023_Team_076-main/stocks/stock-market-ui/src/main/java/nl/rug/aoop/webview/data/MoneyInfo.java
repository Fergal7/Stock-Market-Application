package nl.rug.aoop.webview.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data holder for a monetary data point. Associated with a timestamp and an amount of money.
 * Used by Gson to convert to JSON, hence why there are no getters.
 */
public class MoneyInfo {

    private final String time;
    private final double money;

    /**
     * Creates a new monetary data point with as timestamp the current time.
     *
     * @param money The amount of money associated with this data point.
     */
    public MoneyInfo(double money) {
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d H:m:s.S"));
        this.money = money;
    }
}
