package nl.rug.aoop.networking.client;

import nl.rug.aoop.networking.messages.MessageHandler;
import nl.rug.aoop.networking.messages.OurMessageHandler;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import nl.rug.aoop.networking.TestUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestClient {
    private Socket socket;
    private MessageHandler messageHandler;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Client client;
    private InetSocketAddress address;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        messageHandler = mock(MessageHandler.class);
        socket = mock(Socket.class);
        bufferedReader = mock(BufferedReader.class);
        printWriter = mock(PrintWriter.class);

        // Mock socket interactions
        when(socket.isConnected()).thenReturn(true);
        when(socket.isConnected()).thenReturn(true);
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        CommandHandler commandHandler = new CommandHandler();

        OurMessageHandler ourMessageHandler = new OurMessageHandler(commandHandler);

        client = new Client(new InetSocketAddress("localhost", 8080), messageHandler, "dummyId");


        // Use reflection to bypass initSocket() and set the necessary fields
        TestUtils.setPrivateField(client, "socket", socket);
        TestUtils.setPrivateField(client, "in", bufferedReader);
        TestUtils.setPrivateField(client, "out", printWriter);
        TestUtils.setPrivateField(client, "connected", true);
    }

    @AfterEach
    public void tearDown() throws IOException {
        client.getSocket().close();

        messageHandler = null;
        socket = null;
        bufferedReader = null;
        printWriter = null;
    }

    @Test
    public void testConstructor() throws IOException {
        CommandHandler commandHandler = new CommandHandler();

        OurMessageHandler ourMessageHandler = new OurMessageHandler(commandHandler);

        Client client2 = new Client(new InetSocketAddress("localhost", 8080), ourMessageHandler, "dummyId2");

        assertNotNull(client2);
        assertNotNull(client2.getAddress());
        assertNotNull(client2.getMessageHandler());
    }

    @Test
    public void testTerminateMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("terminate", client.getClass().getMethods()));
    }

    @Test
    public void testRunMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("run", client.getClass().getMethods()));
    }

    @Test
    public void testInitSocketMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("initSocket", client.getClass().getMethods()));
    }

    @Test
    public void testSendMessageMethodAvailability() {
        assertTrue(TestUtils.checkIfMethodExists("sendMessage", client.getClass().getMethods()));
    }

    @Test
    public void TestSendMessageMethod() throws IOException {
        String message = "Hello Server!";
        client.sendMessage(message);
        verify(printWriter).println(message);
    }

    @Test
    public void testTerminate() throws IOException {
        client.terminate();
        assertFalse(client.isRunning());
        assertFalse(client.isConnected());
    }

    @Test
    public void testRunIOException() throws IOException, InterruptedException {
        when(bufferedReader.readLine()).thenThrow(new IOException("Mocked IOException"));
        client.start();
        try {
            Thread.sleep(100); // Allow some time for the thread to run
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(client.isRunning());
        client.terminate();
        client.join();
    }

    @Test
    public void testInitSocket() throws IOException {
        Server server = new Server(50000, null);
        Client client = new Client(new InetSocketAddress("localhost", 50000), new OurMessageHandler(new CommandHandler()), "dummyClientId");

        client.initSocket();
        server.terminate();
        client.terminate();
        assertNotNull(client.getSocket());
    }

    @Test
    public void testRun() throws IOException, InterruptedException {
        client.start();
        try {
            Thread.sleep(100); // Allow some time for the thread to run
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(client.isRunning());
        client.terminate();
        client.join();
    }


}
