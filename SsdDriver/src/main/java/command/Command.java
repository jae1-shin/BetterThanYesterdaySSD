package command;

import command.context.CommandContext;

import java.io.IOException;

public interface Command {
    public void execute(CommandContext commandContext) throws IOException;
}
