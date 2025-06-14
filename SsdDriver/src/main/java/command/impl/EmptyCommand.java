package command.impl;

import command.Command;
import command.context.CommandContext;

import java.io.IOException;

public class EmptyCommand implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        // do nothing
    }
}
