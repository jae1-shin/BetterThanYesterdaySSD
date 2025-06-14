package command.context;

import command.CommandType;

public class EmptyCommandContext extends CommandContext {

    public EmptyCommandContext() {
        super(CommandType.EMPTY);
    }
}
