package nl.rug.aoop.networking.messages;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Temp message handler, doesnt do things yet..
 */
public class OurMessageHandler implements MessageHandler<Void> {
    private final CommandHandler commandHandler;

    /**
     * Temp message handler, doesnt do things yet..
     * @param commandHandler commandHandler that should execute all the right commands when asked
     */
    public OurMessageHandler(CommandHandler commandHandler){
        this.commandHandler = commandHandler;
    }

    @Override
    public Void handleMessage(String message) {
        Message messageObject = Message.createMessageFromString(message);
        String commandName = messageObject.getHeader();

        Map<String, Object> params = new HashMap<>();
        params.put("body", Message.createMessageFromString(messageObject.getBody()));

        commandHandler.executeCommand(commandName, params);
        return null;
    }
}
