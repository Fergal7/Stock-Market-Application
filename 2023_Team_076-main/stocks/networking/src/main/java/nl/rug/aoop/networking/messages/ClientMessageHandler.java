package nl.rug.aoop.networking.messages;

/**
 * Class that handles message to that server sends to client.
 */
public class ClientMessageHandler implements MessageHandler {

    /**
     * Handle message that the client has received from the server.
     *
     * @param message message that the handler receives.
     */
    @Override
    public Void handleMessage(String message) {
        return null;
    }
}
