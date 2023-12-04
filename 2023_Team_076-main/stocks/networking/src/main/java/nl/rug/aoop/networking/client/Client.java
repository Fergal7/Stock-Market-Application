package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messages.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Represents a client class that can communicate with a server using socket connections.
 * The client can send messages to and receive messages from the server.
 */
@Getter
@Setter
@Slf4j
public class Client extends Thread {
    private final MessageHandler messageHandler;
    private static final int TIMEOUT = 1000;
    private final InetSocketAddress address;
    private boolean connected = false;
    private boolean running = false;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private Socket socket;

    /**
     * Constructs a Client instance that can communicate with a server
     * specified by an InetSocketAddress and handle messages using a MessageHandler.
     *
     * @param address           the InetSocketAddress of the server to communicate with.
     * @param messageHandler    handler for processing incoming messages.
     * @param username          Name of the client that connects to the server.
     * @throws IOException      if an I/O error occurs when initializing the client.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler, String username) throws IOException {
        this.address = address;
        this.username = username;
        this.messageHandler = messageHandler;
    }

    /**
     * Initializes the client socket and establishes a connection with the server.
     * Sets up input and output stream for message communication.
     *
     * @throws IOException if an I/O error occurs when connecting to the server.
     */
    public void initSocket() throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);
        if (!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }
        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);

        out.println(this.username);
    }

    /**
     * Continuously listens for messages from the server and logs them.
     * Stops listening if an I/O error occurs or if the client is terminated.
     */
    public void run() {
        running = true;
        while (running) {
            try {
                String fromServer = in.readLine();
                messageHandler.handleMessage(fromServer);
                log.info("Server has sent you the following message: " + fromServer);
            } catch (IOException e) {
                log.error("Could not read line from server " + e);
                try {
                    terminate();
                } catch (IOException ex) {
                    log.error("Could not terminate the socket of " + username);
                }
            }
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param msg the message to be sent to the server.
     * @throws IOException if an I/O error occurs when sending the message.
     */
    public void sendMessage(String msg) throws IOException {
        out.println(msg);
    }

    /**
     * Terminates the client by stopping its listening loop
     * and marking it as disconnected.
     */
    public void terminate() throws IOException {
        socket.close();
        running = false;
        connected = false;
    }

}
