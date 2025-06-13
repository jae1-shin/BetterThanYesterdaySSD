package command;

import logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();
    Logger logger = Logger.getInstance();

    public void register(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public CommandResult execute(String inputLine) {
        String[] parts = inputLine.trim().split("\\s+");

        Command command = commands.get(parts[0].toLowerCase());
        if (command != null) {
            return command.execute(parts);
        } else {
            return CommandResult.error("INVALID COMMAND");
        }
    }

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName);
    }
}
