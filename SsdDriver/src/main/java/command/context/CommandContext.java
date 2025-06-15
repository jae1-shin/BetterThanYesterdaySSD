package command.context;

import command.CommandType;

public abstract class CommandContext {
    protected CommandType type;
    protected int lba;
    protected int size;
    protected String data;
    protected String commandFullName;

    protected CommandContext(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }

    public int getLba() {
        return lba;
    }

    public int getSize() {
        return size;
    }

    public String getData() {
        return data;
    }

    public String getCommandFullName() {
        return commandFullName;
    }

    public boolean isWrite() {
        return type == CommandType.WRITE;
    }

    public boolean isErase() {
        return type == CommandType.ERASE;
    }

    public String toString() {
        return commandFullName;
    }
}
