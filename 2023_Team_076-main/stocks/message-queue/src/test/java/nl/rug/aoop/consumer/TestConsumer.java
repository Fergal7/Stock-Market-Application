package nl.rug.aoop.consumer;

import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;
import nl.rug.aoop.messagequeue.UnorderedQueue;
import nl.rug.aoop.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestConsumer {
    private MQConsumer consumerEmptyQueue;
    private MQConsumer consumerFilledQueue;
    private Message testMessage1;
    private Message testMessage2;

    @BeforeEach
    public void setUp() {
        testMessage1 = new Message("Header", "Message");
        testMessage2 = new Message("Header", "Message");

        MessageQueue messageQueueFull = new UnorderedQueue();
        messageQueueFull.enqueue(testMessage1);
        messageQueueFull.enqueue(testMessage2);
        consumerFilledQueue = new Consumer(messageQueueFull);


        MessageQueue messageQueueEmpty = new UnorderedQueue();
        consumerEmptyQueue = new Consumer(messageQueueEmpty);
    }

    @Test
    public void TestConsumerConstructor() {
        assertNotNull(consumerEmptyQueue);
        assertNotNull(consumerFilledQueue);
    }

    @Test
    public void testPollMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("poll", consumerEmptyQueue.getClass().getMethods()));
    }

    @Test
    public void testPollWhenQueueIsEmpty() {
        assertNull(consumerEmptyQueue.poll());
    }

    @Test
    public void testPollWithMessageInQueue() {
        assertEquals(testMessage1, consumerFilledQueue.poll());
    }

    @Test
    public void testMultiplePolls() {
        assertEquals(testMessage1, consumerFilledQueue.poll());
        assertEquals(testMessage2, consumerFilledQueue.poll());
    }

    @Test
    public void testConstructorWithNullParam(){
        assertThrows(IllegalArgumentException.class, () -> {new Consumer((null));});
    }

    @Test
    public void testQueueEmptyAfterPolling() {
        assertEquals(testMessage1, consumerFilledQueue.poll());
        assertEquals(testMessage2, consumerFilledQueue.poll());
        assertNull(consumerEmptyQueue.poll());
    }

    @Test
    public void testMultipleConsumersPolling() {
        MessageQueue messageQueueFull = new UnorderedQueue();
        messageQueueFull.enqueue(testMessage1);
        messageQueueFull.enqueue(testMessage2);

        Consumer c1 = new Consumer(messageQueueFull);
        Consumer c2 = new Consumer(messageQueueFull);

        assertEquals(testMessage1, c1.poll());
        assertEquals(testMessage2, c2.poll());
    }

    @Test
    public void testPollWithEmptyQueue(){
        assertNull(consumerEmptyQueue.poll());
    }


    @Test
    public void testPollWithNullMessage(){
        MessageQueue messageQueueWithNull = new UnorderedQueue();
        messageQueueWithNull.enqueue(null);
        MQConsumer consumerWithNull = new Consumer(messageQueueWithNull);

        assertNull(consumerWithNull.poll());
    }
}
