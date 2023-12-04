package nl.rug.aoop.command;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code CommandHandler} class is responsible for registering and executing commands.
 */
public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Register a command with a given name.
     *
     * @param commandName The name of commad.
     * @param command     The command.
     */
    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Execute a registered command with the given name and parameters.
     *
     * @param commandName The name of the command to execute.
     * @param params      A map of parameters to pass to the command.
     * @throws IllegalArgumentException if the  command is not registered.
     */
    public void executeCommand(String commandName, Map<String, Object> params) {
        if (commands.containsKey(commandName)) {
            commands.get(commandName).execute(params);
        } else {
            throw new IllegalArgumentException("Command not registered: " + commandName);
        }
    }
}
