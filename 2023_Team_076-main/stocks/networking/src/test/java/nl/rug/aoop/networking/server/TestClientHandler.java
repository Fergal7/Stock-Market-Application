package nl.rug.aoop.networking.server;

import nl.rug.aoop.networking.TestUtils;
import nl.rug.aoop.networking.messages.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestClientHandler {
    private ClientHandler clientHandler;

    @BeforeEach
    public void setUp() throws IOException {
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        MessageHandler messageHandler = mock(MessageHandler.class);

        clientHandler = new ClientHandler(socket, 1, messageHandler);
    }

    @Test
    public void testRunMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("run", clientHandler.getClass().getMethods()));
    }

    @Test
    public void testTerminateMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("terminate", clientHandler.getClass().getMethods()));
    }

    @Test
    public void TestConstructor(){
        assertNotNull(clientHandler);
        assertNotNull(clientHandler.getSocket());
        assertNotNull(clientHandler.getMessageHandler());
        assertNotNull(clientHandler.getIn());
        assertNotNull(clientHandler.getOut());
        assertEquals(clientHandler.getThreadId(), 1);
        assertFalse(clientHandler.isRunning());
    }

    @Test
    public void TestTerminateWhenNotRunning() {
        assertFalse(clientHandler.isRunning());
        clientHandler.terminate();
        assertFalse(clientHandler.isRunning());
    }

    @Test
    public void TestTerminateWhenRunning() throws NoSuchFieldException, IllegalAccessException {
        TestUtils.setPrivateField(clientHandler, "running", true);
        assertTrue(clientHandler.isRunning());
        clientHandler.terminate();
        assertFalse(clientHandler.isRunning());
    }
}