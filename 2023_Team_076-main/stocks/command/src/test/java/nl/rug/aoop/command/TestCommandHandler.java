/**
 * Test class for the {@link CommandHandler} class, which is responsible for registering and executing commands.
 */
package nl.rug.aoop.command;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for the {@link CommandHandler} class, which is responsible for registering and executing commands.
 */
public class TestCommandHandler {

    private CommandHandler commandHandler;

    /**
     * Initializes a new instance of the {@link CommandHandler} before each test method.
     */
    @Before
    public void setUp() {
        commandHandler = new CommandHandler();
    }

    /**
     * Tests the registration of a command .
     */
    @Test
    public void testRegisterCommand() {
        Command mockCommand = mock(Command.class);
        commandHandler.registerCommand("testCommand", mockCommand);
    }

    /**
     * Tests the execution of a registered command.
     */
    @Test
    public void testExecuteCommand() {
        Command mockCommand = mock(Command.class);
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");

        commandHandler.registerCommand("testCommand", mockCommand);
        commandHandler.executeCommand("testCommand", params);

        verify(mockCommand).execute(params);
    }

    /**
     * Tests the execution of an unregistered command with the {@link CommandHandler}, expecting an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExecuteUnregisteredCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");

        commandHandler.executeCommand("nonExistentCommand", params);
    }
}
