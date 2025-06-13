package command.context;

import command.CommandType;

public class FlushCommandContext extends CommandContext {

    public FlushCommandContext() {
        super(CommandType.FLUSH);
        this.commandFullName = "F";
    }
}
