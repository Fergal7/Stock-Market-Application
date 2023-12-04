package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messages.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents a server that can accept and manage connections from multiple clients.
 * Implements Runnable to manage client connections in a separate thread.
 */
@Slf4j
@Getter
public class Server implements Runnable {
    private boolean running = false;
    private int threadId = 0;
    private final int port;
    private final MessageHandler<Void> messageHandler;
    private final ArrayList<ClientHandler> clients;
    private final ServerSocket serverSocket;
    private final ExecutorService service;

    /**
     * Constructs a new Server instance, initializing a ServerSocket on the specified port
     * and preparing an ExecutorService to manage client handlers.
     *
     * @param port the port on which the server should listen for connections.
     * @param messageHandler handler interface that will handle the data that the clients send to the server
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public Server(int port, MessageHandler<Void> messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        this.port = port;

        service = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    /**
     * Continuously listens for client connections, assigning each to a ClientHandler
     * managed by a separate thread from an ExecutorService.
     * Logs an info message upon accepting a new connection and an error message if a
     * socket error occurs.
     */
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                Socket socket = this.serverSocket.accept();
                log.info("New connection from client");

                ClientHandler clientHandler = new ClientHandler(socket, threadId, messageHandler);
                clients.add(clientHandler);
                this.service.submit(clientHandler);
                threadId++;
            } catch (IOException e) {
                log.error("Socket error: ", e);
            }
        }
    }

    /**
     * Method that sends a message to a specific connected client.
     * @param message test
     * @param userId id of the client you want to send something to.
     */
    public void serverSendMessageToSpecificClient(String message, String userId) {
        clients.forEach(client -> {
            if (Objects.equals(client.getUserId(), userId)) {
                client.sendMessage(message);
            }
        });
    }

    /**
     * Method that sends a message to all the connected clients.
     *
     * @param message message that you want to send to the client
     */
    public void serverSendMessageToEveryClient(String message) {
        clients.forEach(client -> {
            client.sendMessage(message);
        });
    }

    /**
     * Terminates the server, shutting down the ExecutorService and closing the ServerSocket.
     *
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    public void terminate() throws IOException {
        running = false;
        this.service.shutdown();
        this.serverSocket.close();

        clients.forEach(ClientHandler::terminate);
    }

}
