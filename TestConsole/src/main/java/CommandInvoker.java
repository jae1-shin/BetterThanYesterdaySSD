import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public void execute(String inputLine) {
        String[] parts = inputLine.trim().split("\\s+");
        if (parts.length == 0) return;

        Command command = commands.get(parts[0].toLowerCase());
        if (command != null) {
            command.execute(parts);
        } else {
            System.out.println("INVALID COMMAND");
        }
    }
}
