package nl.rug.aoop.producer;

import nl.rug.aoop.messagequeue.Message;

/**
 * Represents the interface of a producer that populates the MessageQueue.
 */
public interface MQProducer {
    /**
     * Puts data in the MessageQueue, so it can be used by a consumer later.
     * @param message message instance that contains a message the producer made
     */
    void put(Message message);
}
