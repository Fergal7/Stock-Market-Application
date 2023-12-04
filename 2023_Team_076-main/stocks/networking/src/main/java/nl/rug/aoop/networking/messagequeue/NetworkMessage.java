package nl.rug.aoop.networking.messagequeue;

import lombok.Getter;
import nl.rug.aoop.messagequeue.Message;

/**
 * Message used in the communication from client to server. Just a simple wrapper for normal message
 */
@Getter
public class NetworkMessage {
    private final String header;
    private final String body;

    /**
     * Constructs a new {@code NetworkMessage} with the specified header and body.
     *
     * @param header The header
     * @param body   The body
     */
    public NetworkMessage(String header, String body) {
        this.header = header;
        this.body = body;
    }

    /**
     * Converts the {@code NetworkMessage} to its JSON representation.
     *
     * @return A JSON representation of the {@code NetworkMessage}.
     */
    public String toJson() {
        Message message = new Message(header, body);
        return message.toJson();
    }

    /**
     * Creates a new NetworkMessage with a predefined header "MqPut" and JSON representation of a provided Message.
     *
     * @param message The Message to include in body.
     * @return A new NetworkMessage instance with the "MqPut" header.
     */
    public static NetworkMessage createPutMessage(Message message) {
        return new NetworkMessage("MqPut", message.toJson());
    }
}
