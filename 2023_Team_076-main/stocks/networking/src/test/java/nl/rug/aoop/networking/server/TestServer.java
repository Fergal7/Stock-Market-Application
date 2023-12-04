package nl.rug.aoop.networking.server;

import nl.rug.aoop.networking.TestUtils;
import nl.rug.aoop.networking.messages.MessageHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestServer {
    private static Server server;
    private static final int port = 9004;
    @BeforeAll
    public static void setUp() throws IOException {
        MessageHandler<Void> messageHandler = mock(MessageHandler.class);
        server = new Server(port, messageHandler);
    }

    @AfterEach
    public void cleanUp() throws IOException {
        if (server != null) {
            server.terminate();
            server.getServerSocket().close();
        }
    }

    @Test
    public void TestConstructor() {
        assertNotNull(server);
        assertNotNull(server.getServerSocket());
        assertNotNull(server.getService());
        assertEquals(server.getPort(), port);
        assertEquals(server.getThreadId(), 0);
        assertFalse(server.isRunning());

    }

    @Test
    public void testRunMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("run", server.getClass().getMethods()));
    }

    @Test
    public void testTerminateMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("terminate", server.getClass().getMethods()));
    }

    @Test
    public void testTerminateFunctionWhileRunning() throws NoSuchFieldException, IllegalAccessException, IOException {
        TestUtils.setPrivateField(server, "running", true);
        assertTrue(server.isRunning());
        server.terminate();
        assertFalse(server.isRunning());
        assertTrue(server.getService().isShutdown());
    }

    @Test
    public void testTerminateFunctionWhileNotRunning() throws NoSuchFieldException, IllegalAccessException, IOException {
        assertFalse(server.isRunning());
        server.terminate();
        assertFalse(server.isRunning());
        assertTrue(server.getService().isShutdown());
    }

    @Test
    public void testServerAcceptsConnection() throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        ServerSocket serverSocketMock = mock(ServerSocket.class);
        when(serverSocketMock.accept()).thenReturn(null); // To stop the server

        TestUtils.setPrivateField(server, "serverSocket", serverSocketMock);

        Thread serverThread = new Thread(server);
        serverThread.start();
        serverThread.join();

        verify(serverSocketMock, times(1)).accept();
    }

    @Test
    public void testServerTermination() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        ServerSocket serverSocketMock = mock(ServerSocket.class);
        ExecutorService serviceMock = mock(ExecutorService.class);
        when(serverSocketMock.accept()).thenThrow(new SocketTimeoutException("Timeout for testing purpose"));

        TestUtils.setPrivateField(server, "serverSocket", serverSocketMock);
        TestUtils.setPrivateField(server, "service", serviceMock);

        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread.sleep(100);
        server.terminate();
        serverThread.join();

        verify(serviceMock, times(1)).shutdown();
        assertFalse(server.isRunning());
    }


}
