import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public boolean execute(String inputLine) {
        String[] parts = inputLine.trim().split("\\s+");
        if (parts.length == 0) return false;

        Command command = commands.get(parts[0].toLowerCase());
        if (command != null) {
            return command.execute(parts);
        } else {
            System.out.println("INVALID COMMAND");
            return false;
        }
    }

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName);
    }
}
