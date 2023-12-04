package nl.rug.aoop.producer;

import lombok.Getter;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;

/**
 * Represents an implementation of a basic producer that populates the MessageQueue.
 */
@Getter
public class Producer implements MQProducer{
    private final MessageQueue messageQueue;

    /**
     * Constructs a new Producer and initializes it with a MessageQueue.
     * @param messageQueue MessageQueue instance
     */
    public Producer(MessageQueue messageQueue) {
        if (messageQueue == null) {
            throw new IllegalArgumentException();
        }
        this.messageQueue = messageQueue;
    }

    @Override
    public void put(Message message) {
        this.messageQueue.enqueue(message);
    }
}
