package nl.rug.aoop.consumer;

import nl.rug.aoop.messagequeue.Message;

/**
 * Represents the interface of a consumer that gets info from the MessageQueue.
 */
public interface MQConsumer {
    /**
     * Polls a data out of a MessageQueue, so the consumer can use it.
     * @return returns message object put in the queue by a producer
     */
    Message poll();
}
