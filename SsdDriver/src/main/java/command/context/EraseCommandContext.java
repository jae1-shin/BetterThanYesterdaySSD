package command.context;

import command.CommandType;

public class EraseCommandContext extends CommandContext {

    public EraseCommandContext(int lba, int size) {
        super(CommandType.ERASE);
        this.lba = lba;
        this.size = size;
        this.commandFullName = String.format("E_%d_%d", lba, size);
    }
}
