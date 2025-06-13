package command;

public class CommandContext {
    int order;
    CommandType type;
    int lba;
    int size;
    String data;
    String commandFullName;

    public CommandContext(int order, CommandType type, int lba, int size, String data, String commandFullName) {
        this.order = order;
        this.type = type;
        this.lba = lba;
        this.size = size;
        this.data = data;
        this.commandFullName = commandFullName;
    }

    public int getOrder() {
        return order;
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
}
