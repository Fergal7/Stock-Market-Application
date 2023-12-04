package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import nl.rug.aoop.TestUtils;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
    }

    @Test
    public void testEnqueueMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("enqueue", queue.getClass().getMethods()));
    }

    @Test
    public void testDequeueMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("dequeue", queue.getClass().getMethods()));
    }

    @Test
    public void testGetSizeMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("getSize", queue.getClass().getMethods()));
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
    }

    @Test
    void testQueueEnqueueNullMessage(){
        int initalSize = queue.getSize();
        queue.enqueue(null);
        int finalSize = queue.getSize();
        assertEquals(initalSize, finalSize);
    }

    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testQueueEnqueueDequeueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

    @Test
    void testDequeueOnEmptyQueue() {
        assertEquals(0, queue.getSize());

        assertNull(queue.dequeue());
        assertNull(queue.dequeue());
        assertNull(queue.dequeue());
    }

    @Test
    void testQueueEnqueueNothing() {
        int beginSize = queue.getSize();
        queue.enqueue(null);
        int endSize = queue.getSize();
        assertEquals(beginSize, endSize);
    }

    @Test
    void testGetSize() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }
}
