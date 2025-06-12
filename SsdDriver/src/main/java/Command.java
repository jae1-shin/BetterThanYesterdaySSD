public class Command {
    int order;
    CommandType type;
    int lba;
    int size;
    String data;

    Command(int order, CommandType type, int lba, int size, String data) {
        this.order = order;
        this.type = type;
        this.lba = lba;
        this.size = size;
        this.data = data;
    }
}
