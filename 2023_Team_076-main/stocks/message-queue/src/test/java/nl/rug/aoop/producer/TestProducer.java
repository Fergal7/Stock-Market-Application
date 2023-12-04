package nl.rug.aoop.producer;

import nl.rug.aoop.TestUtils;
import nl.rug.aoop.consumer.Consumer;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;
import nl.rug.aoop.messagequeue.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestProducer {
    private Producer producerEmptyQueue;
    private Producer producerFilledQueue;

    @BeforeEach
    public void setUp() {
        MessageQueue messageQueueFull = new UnorderedQueue();
        messageQueueFull.enqueue(new Message("Header", "Message"));
        messageQueueFull.enqueue(new Message("Header", "Message"));
        producerFilledQueue = new Producer(messageQueueFull);

        MessageQueue messageQueueEmpty = new UnorderedQueue();
        producerEmptyQueue = new Producer(messageQueueEmpty);
    }

    @Test
    public void testPutMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("put", producerEmptyQueue.getClass().getMethods()));
    }

    @Test
    public void TestConsumerConstructor() {
        assertNotNull(producerEmptyQueue);
        assertNotNull(producerFilledQueue);
    }

    @Test
    public void testConstructorWithNullParam(){
        assertThrows(IllegalArgumentException.class, () -> {new Producer((null));});
    }

    @Test
    public void testPutMessageOnEmpty() {
        producerEmptyQueue.put(new Message("Header", "Message"));
        assertEquals(producerEmptyQueue.getMessageQueue().getSize(),1);
    }

    @Test
    public void testPutMessageOnFilled() {
        producerFilledQueue.put(new Message("Header", "Message"));
        assertEquals(producerFilledQueue.getMessageQueue().getSize(),3);
    }

    @Test
    public void testPutMessageOnMultipleTimes() {
        producerEmptyQueue.put(new Message("Header", "Message"));
        producerEmptyQueue.put(new Message("Header", "Message"));
        assertEquals(producerEmptyQueue.getMessageQueue().getSize(),2);
    }

    @Test
    public void testPutNullMessage() {
        int beginSize = producerEmptyQueue.getMessageQueue().getSize();
        producerEmptyQueue.put(null);
        int endSize = producerEmptyQueue.getMessageQueue().getSize();
        assertEquals(beginSize, endSize);
    }
}
