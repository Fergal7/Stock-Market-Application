package nl.rug.aoop.messagequeue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents an unordered message queue where messages are not sorted based on timestamps.
 */
public class UnorderedQueue implements MessageQueue {
    private final Queue<Message> messageQueue;

    /**
     * Constructs a new UnorderedQueue and initializes it with a LinkedList.
     */
    public UnorderedQueue() {
        this.messageQueue = new LinkedList<>();
    }

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            this.messageQueue.add(message);
        }
    }

    @Override
    public Message dequeue() {
        if(this.messageQueue.isEmpty()) {
            return null;
        }
        return this.messageQueue.remove();
    }

    @Override
    public int getSize() {
        return this.messageQueue.size();
    }
}
