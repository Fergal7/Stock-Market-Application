package nl.rug.aoop.Transaction;

import lombok.Getter;

/**
 * Represents a stock market transaction.
 */
@Getter
public class Transaction {
    private final String type;
    private final String stock;
    private final int amountSold;
    private final int sharePrice;

    /**
     * Constructs a Transaction instance.
     *
     * @param type        The type of the transaction.
     * @param stock       The name or symbol of the stock involved in the transaction.
     * @param amountSold  The quantity of stocks sold.
     * @param sharePrice  The price per share at the time of the transaction.
     */
    public Transaction(String type, String stock, int amountSold, int sharePrice) {
        this.type = type;
        this.stock = stock;
        this.amountSold = amountSold;
        this.sharePrice = sharePrice;
    }
}
