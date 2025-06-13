package command.context;

import command.CommandType;

public class EmptyCommandContext extends CommandContext{

    protected EmptyCommandContext() {
        super(CommandType.EMPTY);
        this.commandFullName = "empty";
    }
}
