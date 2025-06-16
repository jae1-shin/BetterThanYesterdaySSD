package command.context;

import command.CommandType;

public class WriteCommandContext extends CommandContext {

    public WriteCommandContext(int lba, String data) {
        super(CommandType.WRITE);
        this.lba = lba;
        this.data = data;
        this.commandFullName = String.format("W_%d_%s", lba, data);
    }
}
