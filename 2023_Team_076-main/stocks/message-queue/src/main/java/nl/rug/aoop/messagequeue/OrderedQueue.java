package nl.rug.aoop.messagequeue;

import java.util.*;

/**
 * Represents an ordered message queue that maintains messages based on their timestamps.
 */

public class OrderedQueue implements MessageQueue {
    private int count = 0;

    private final PriorityQueue<Entry> messageQueue;

    /**
     * Constructs a new OrderedQueue and initializes it with a PriorityQueue that will
     * place the items based on their timestamp and entry number if they have the same timestamp.
     */
    public OrderedQueue() {
        messageQueue = new PriorityQueue<>(Comparator
                .comparing((Entry entry) -> entry.message().getTimestamp())
                .thenComparing(Entry::entryNumber));
    }

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            messageQueue.offer(new Entry(message, count++));
        }
    }

    @Override
    public Message dequeue() {
        if (messageQueue.isEmpty()) {
            return null;
        }
        return messageQueue.poll().message;
    }

    @Override
    public int getSize() {
        return messageQueue.size();
    }

    private record Entry(Message message, int entryNumber) { }
}

