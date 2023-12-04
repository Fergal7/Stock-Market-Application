package nl.rug.aoop.networking.messagequeue;

import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.OrderedQueue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Represents a thread-safe version of an ordered message queue that maintains messages based on their timestamps.
 */
public class NetworkOrderedQueue extends OrderedQueue {
    private PriorityBlockingQueue<Entry> messageQueue;

    /**
     * Constructs a new NetworkOrderedQueue and initializes it with a PriorityBlockingQueue that will
     * place the items based on their timestamp and entry number if they have the same timestamp. This is also
     * thread-safe, so this version can be used in the networking assignment.
     */
    public NetworkOrderedQueue() {
        messageQueue = new PriorityBlockingQueue<>(10, Comparator
                .comparing((Entry entry) -> entry.message().getTimestamp())
                .thenComparing(Entry::entryNumber));
    }

    private record Entry(Message message, int entryNumber) { }
}

