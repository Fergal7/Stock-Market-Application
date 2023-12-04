package nl.rug.aoop.networking.messages;

/**
 * Defines the contract for handling messages. This interface can be implemented by classes that
 * need to specify a strategy for handling incoming messages.
 *
 * @param <T> the type of result produced by handling a message.
 */
public interface MessageHandler<T> {
    /**
     * Processes the specified message and returns a result.
     * Implementers define the handling logic in this method,
     * ensuring it adheres to the expected behavior or protocol.
     *
     * @param message the message to be handled, represented as a String.
     * @return the result of handling the message, of type T.
     */
    T handleMessage(String message);
}
