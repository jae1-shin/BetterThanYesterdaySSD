import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SsdEraser implements SsdCommand {

    @Override
    public void execute(Command command) throws IOException {
        erase(command.getLba(), command.getSize());
    }

    public void erase(int address, int size) throws IOException {
        File file = new File(SsdConstants.SSD_NAND_FILE);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(address * SsdConstants.BLOCK_SIZE);
        raf.writeBytes(SsdConstants.DEFAULT_DATA.repeat(size));
        raf.close();
    }
}
