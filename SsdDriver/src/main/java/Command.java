public class Command {
    int order;
    CommandType type;
    int lba;
    int size;
    String data;
    String commandFullName;

    Command(int order, CommandType type, int lba, int size, String data, String commandFullName) {
        this.order = order;
        this.type = type;
        this.lba = lba;
        this.size = size;
        this.data = data;
        this.commandFullName = commandFullName;
    }
}
