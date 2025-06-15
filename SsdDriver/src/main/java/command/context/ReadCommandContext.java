package command.context;

import command.CommandType;

public class ReadCommandContext extends CommandContext {

    public ReadCommandContext(int lba) {
        super(CommandType.READ);
        this.lba = lba;
        this.commandFullName = String.format("R_%d", lba);
    }
}
