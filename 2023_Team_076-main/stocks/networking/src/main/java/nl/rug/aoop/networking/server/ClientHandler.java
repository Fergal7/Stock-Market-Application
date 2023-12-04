package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messages.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages communication with a connected client by handling
 * input/output through the client's socket and ensuring message
 * handling according to the provided MessageHandler.
 */
@Slf4j
@Getter
public class ClientHandler implements Runnable {
    private final MessageHandler messageHandler;
    private boolean running = false;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    private final int threadId;
    private String userId;

    /**
     * Initializes a new ClientHandler for managing communication through
     * the provided socket. It also initializes input and output streams associated
     * with the socket.
     *
     * @param socket         the socket connected to the client.
     * @param id             an identifier for the client.
     * @param messageHandler the handler responsible for processing messages.
     * @throws IOException if an I/O error occurs when creating input/output streams.
     */
    public ClientHandler(Socket socket, int id, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        this.socket = socket;
        this.threadId = id;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Handles communication with the client, greeting them upon connection,
     * listening for messages, and managing disconnection.
     * Continuously reads messages from the client, logging received messages,
     * and terminates connection if the client sends null as a message.
     */
    @Override
    public void run() {
        running = true;

        try {
            this.userId = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(running) {
            try {
                String fromClient = in.readLine();
                messageHandler.handleMessage(fromClient);
                log.info("Received from client " + threadId + " " + fromClient);
            } catch (IOException e) {
                running = false;
            }
        }
    }

    /**
     * Sends message to the client socket.
     * @param message the gets send to the client.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Terminates the client handler, closing the client socket and
     * stopping the listening loop.
     */
    public void terminate() {
        running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket ", e);
        }
    }
}
