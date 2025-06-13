import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class SsdCommandService {
    private static final Map<CommandType, SsdCommand> commandMap = new EnumMap<>(CommandType.class);

    static {
        commandMap.put(CommandType.WRITE, new SsdWriter());
        commandMap.put(CommandType.ERASE, new SsdEraser());
        commandMap.put(CommandType.READ, new SsdReader());
        commandMap.put(CommandType.FLUSH, new SsdFlush());
    }

    public static void execute(Command command) {
        try {
            commandMap.get(command.getType()).execute(command);
        } catch (IOException e) {
            // ignore
        }
    }
}
