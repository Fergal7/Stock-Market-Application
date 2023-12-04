package nl.rug.aoop.networking.messagequeue;

import static org.mockito.Mockito.*;

import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains JUnit tests for the {@link MqPutCommand} class.
 */
public class MqPutCommandTest {
    private MqPutCommand mqPutCommand;
    private MessageQueue messageQueue;

    /**
     * Initializes the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        messageQueue = mock(MessageQueue.class);
        mqPutCommand = new MqPutCommand(messageQueue);
    }

    /**
     * Test the execute method of the {@link MqPutCommand} class.
     * This test verifies that the execute method enqueues a message into the message queue.
     */
    @Test
    void executeShouldEnqueueMessage() {
        String header = "TestHeader";
        String body = "TestBody";
        Message testMessage = new Message(header, body);

        Map<String, Object> params = new HashMap<>();
        params.put("body", testMessage);

        mqPutCommand.execute(params);

        verify(messageQueue, times(1)).enqueue(testMessage);
    }
}