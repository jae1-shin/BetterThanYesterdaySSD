package command;

import command.buffer.BufferEraser;
import command.buffer.BufferReader;
import command.buffer.BufferWriter;
import command.context.CommandContext;
import command.impl.EmptyCommand;
import command.impl.FlushCommand;
import command.impl.ReadCommand;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<CommandType, Command> commandMap = new EnumMap<>(CommandType.class);

    static {
        commandMap.put(CommandType.WRITE, new BufferWriter());
        commandMap.put(CommandType.ERASE, new BufferEraser());
        commandMap.put(CommandType.READ, new BufferReader(new ReadCommand()));
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
