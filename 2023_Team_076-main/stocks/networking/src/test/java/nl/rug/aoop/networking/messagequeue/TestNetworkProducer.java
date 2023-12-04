package nl.rug.aoop.networking.messagequeue;

import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.networking.TestUtils;
import nl.rug.aoop.networking.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestNetworkProducer {
    private Client client;
    private Message message;
    private NetworkProducer producer;

    @BeforeEach
    public void setUp() throws IOException {
        client = mock(Client.class);
        message = mock(Message.class);
        when(message.toJson()).thenReturn("{messageJson}");

        producer = new NetworkProducer(client);
    }

    @Test
    public void TestHasPutMethod() {
        TestUtils.checkIfMethodExists("put", producer.getClass().getMethods());
    }

    @Test
    public void TestConstructor() {
        assertNotNull(producer);
        assertNotNull(producer.getClient());
    }

    @Test
    public void TestSendMessage() {
        try {
            producer.put(message);
            assert true;
        } catch (Exception generic_exception) {
            fail();
        }
    }

    @Test void TestSendNullMessage() {
        assertThrows(NullPointerException.class, () -> {
            producer.put(null);
        });
    }
}
