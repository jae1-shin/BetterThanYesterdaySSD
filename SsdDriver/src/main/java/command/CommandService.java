package command;

import command.impl.buffer.EraseBufferCommand;
import command.impl.buffer.ReadBufferCommand;
import command.impl.buffer.WriteBufferCommand;
import command.context.CommandContext;
import command.impl.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class CommandService {
    private static final Map<CommandType, Command> commandMap = new EnumMap<>(CommandType.class);

    static {
        commandMap.put(CommandType.WRITE, new WriteBufferCommand());
        commandMap.put(CommandType.ERASE, new EraseBufferCommand());
        commandMap.put(CommandType.READ, new ReadBufferCommand());
        commandMap.put(CommandType.FLUSH, new FlushCommand());
        commandMap.put(CommandType.EMPTY, new EmptyCommand());
    }

    public static void execute(CommandContext commandContext) {
        try {
            commandMap.get(commandContext.getType()).execute(commandContext);
        } catch (IOException e) {
            // ignore
        }
    }
}
