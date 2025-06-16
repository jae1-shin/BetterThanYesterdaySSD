package command.impl.buffer;

import command.Command;
import command.context.CommandContext;

import java.io.IOException;

public class WriteBufferCommand implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        BufferOptimizer.getInstance().processCommand(commandContext);
    }
}
