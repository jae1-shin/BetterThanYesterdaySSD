public class Command {
    enum Type { WRITE, ERASE }

    int order;
    Type type;
    int lba;
    int size;
    String data;

    Command(int order, Type type, int lba, int size, String data) {
        this.order = order;
        this.type = type;
        this.lba = lba;
        this.size = size;
        this.data = data;
    }
}
