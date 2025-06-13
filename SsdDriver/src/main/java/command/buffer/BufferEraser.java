package command.buffer;

import command.Command;
import command.CommandContext;

import java.io.IOException;

public class BufferEraser implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        BufferOptimizer.getInstance().processCommand(commandContext);
    }
}
