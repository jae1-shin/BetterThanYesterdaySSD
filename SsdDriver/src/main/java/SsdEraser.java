import java.io.IOException;

public class SsdEraser {
    public void erase(int address, int size) throws IOException {
        for (int lba = address; lba < address + size; lba++) {
            SsdWriter writer = new SsdWriter();
            writer.write(lba, SsdConstants.DEFAULT_DATA);
        }
    }
}
