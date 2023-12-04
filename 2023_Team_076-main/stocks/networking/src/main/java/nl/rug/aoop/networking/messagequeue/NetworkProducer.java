package nl.rug.aoop.networking.messagequeue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.producer.MQProducer;

import java.io.IOException;


/**
 * A producer in a message queuing context that utilizes network communication.
 * Utilizes a Client instance to send messages via network communication
 * and is capable of converting messages to a JSON format before sending.
 */
@Slf4j
@Getter
public class NetworkProducer implements MQProducer {
    private final Client client;

    /**
     * Constructs a new NetworkProducer utilizing the specified client for message sending.
     * @param client the client responsible for network communication.
     */
    public NetworkProducer(Client client) {
        this.client = client;
    }

    /**
     * Sends a provided message to a message queue by utilizing the client's sendMessage method,
     * after converting the message to its JSON representation.
     * If the message is null, a NullPointerException is thrown.
     * If an I/O error occurs during message sending, an error log is recorded.
     *
     * @param message the message to be sent.
     * @throws NullPointerException if the provided message is {@code null}.
     */
    @Override
    public void put(Message message) {
        if (message != null) {
            try {
                client.sendMessage(NetworkMessage.createPutMessage(message).toJson());
            } catch (IOException e) {
                log.error("Could not send message for the messageQueue ", e);
            }
        } else {
            throw new NullPointerException();
        }

    }
}
