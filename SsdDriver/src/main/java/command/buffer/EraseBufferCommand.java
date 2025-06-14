package command.buffer;

import command.Command;
import command.context.CommandContext;

import java.io.IOException;

public class EraseBufferCommand implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        BufferOptimizer.getInstance().processCommand(commandContext);
    }
}
