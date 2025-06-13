import java.io.IOException;

public class SsdEraser implements SsdCommand {

    @Override
    public void execute(Command command) throws IOException {
        erase(command.getLba(), command.getSize());
    }

    public void erase(int address, int size) throws IOException {
        for (int lba = address; lba < address + size; lba++) {
            SsdWriter writer = new SsdWriter();
            writer.write(lba, SsdConstants.DEFAULT_DATA);
        }
    }
}
