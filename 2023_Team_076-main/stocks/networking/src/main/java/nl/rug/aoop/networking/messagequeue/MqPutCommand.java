package nl.rug.aoop.networking.messagequeue;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.MessageQueue;

import java.util.Map;

/**
 * The MqPutCommand class represents a command to enqueue a message into a MessageQueue.
 */
public class MqPutCommand implements Command {
    private final MessageQueue messageQueue;

    /**
     * Constructs a new MqPutCommand with the given MessageQueue.
     *
     * @param queue The MessageQueue where messages will be enqueued.
     */
    public MqPutCommand(MessageQueue queue) {
        this.messageQueue = queue;
    }

    /**
     * Executes the MqPutCommand enqueuing the specified message into MessageQueue.
     *
     * @param params A map of parameters
     */
    @Override
    public void execute(Map<String, Object> params) {
        Message message = (Message) params.get("body");
        this.messageQueue.enqueue(message);
    }
}