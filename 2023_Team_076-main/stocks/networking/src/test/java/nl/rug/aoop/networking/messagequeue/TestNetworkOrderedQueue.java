package nl.rug.aoop.networking.messagequeue;
import static org.junit.jupiter.api.Assertions.*;

import nl.rug.aoop.messagequeue.Message;
import org.junit.jupiter.api.Test;

public class TestNetworkOrderedQueue {

    @Test
    public void testQueueOrdering() {
        NetworkOrderedQueue queue = new NetworkOrderedQueue();

        // Create messages with different timestamps
        Message message1 = new Message("header1", "body1");
        Message message2 = new Message("header2", "body2");
        Message message3 = new Message("header3", "body3");

        // Enqueue messages in an unordered fashion
        queue.enqueue(message2);
        queue.enqueue(message3);
        queue.enqueue(message1);

        // The messages should be dequeued in the order message1, message2, message3
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }
}
