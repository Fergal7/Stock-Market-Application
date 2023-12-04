package nl.rug.aoop.consumer;

import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;

/**
 * Represents the basic implementation of a consumer that gets info from the MessageQueue.
 */
public class Consumer implements MQConsumer{
    private final MessageQueue messageQueue;

    /**
     * Constructs a new Consumer and initializes it with a MessageQueue.
     * @param messageQueue MessageQueue instance
     */
    public Consumer(MessageQueue messageQueue) {
        if (messageQueue == null) {
            throw new IllegalArgumentException();
        }
        this.messageQueue = messageQueue;
    }

    @Override
    public Message poll() {
        return messageQueue.dequeue();
    }
}
