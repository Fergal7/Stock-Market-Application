package nl.rug.aoop.command;

import java.util.Map;

/**
 * The {@code Command} interface represents a command that can be executed with a set of parameters.
 */
public interface Command {
    /**
     * Execute the command with the specified parameters.
     *
     * @param params A map of parameters to be used by the command.
     */
    void execute(Map<String, Object> params);
}
