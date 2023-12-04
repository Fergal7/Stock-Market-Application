package nl.rug.aoop.messagequeue;

/**
 * Represents a message queue with enqueue, dequeue, and getSize operations.
 */
public interface MessageQueue {
    /**
     * Adds a message to the message queue.
     *
     * @param message The message to be added to the queue.
     */
    void enqueue(Message message);

    /**
     * Retrieves and removes the first message from the message queue.
     *
     * @return The first message in the queue, or null if the queue is empty.
     */
    Message dequeue();

    /**
     * Returns the number of messages currently in the message queue.
     *
     * @return The number of messages in the queue.
     */
    int getSize();
}
