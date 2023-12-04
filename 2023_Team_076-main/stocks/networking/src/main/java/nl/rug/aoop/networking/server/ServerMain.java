package nl.rug.aoop.networking.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.consumer.Consumer;
import nl.rug.aoop.networking.messagequeue.MqPutCommand;
import nl.rug.aoop.networking.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.networking.messages.MessageHandler;
import nl.rug.aoop.networking.messages.OurMessageHandler;

/**
 * Server main for testing the server side of the socket connection.
 */
@Slf4j
public class ServerMain {

    /**
     * Main to test server side of things.
     * @param args args for main
     */
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            NetworkOrderedQueue queue = new NetworkOrderedQueue();

            CommandHandler commandHandler = new CommandHandler();
            commandHandler.registerCommand("MqPut", new MqPutCommand(queue));
            MessageHandler<Void> messageHandler = new OurMessageHandler(commandHandler);

            Server server = new Server(6200, messageHandler);
            service.submit(server);
            log.info("Server started at port " + server.getPort());
            Thread.sleep(10000);

            System.out.println("Queue size: " + queue.getSize());
            Consumer c = new Consumer(queue);
            String message = c.poll().getHeader();
            System.out.println(message);
            System.out.println("Queue size: " + queue.getSize());
            message = c.poll().getHeader();
            System.out.println(message);
            System.out.println("Queue size: " + queue.getSize());
            server.terminate();
        } catch (IOException e) {
            log.error("Could not start server" + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
