import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SsdWriter {

    public static final String DEFAULT_DATA = "0x00000000";
    public static final String SSD_NAND_FILE = "ssd_nand.txt";
    public static final int MAX_DATA_COUNT = 100;
    public static final int BLOCK_SIZE = 10;

    public void write(int address, String data) throws IOException {
        checkFileAndWriteDefaultData();
        writeData(address, data);
    }

    private void checkFileAndWriteDefaultData() throws IOException {
        File file = new File(SSD_NAND_FILE);
        if (file.exists()) return;
        writeDefaultData();
    }

    private void writeData(long address, String data) throws IOException {
        File file = new File(SSD_NAND_FILE);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(address * BLOCK_SIZE);
        raf.writeBytes(data);
        raf.close();
    }

    private void writeDefaultData() throws IOException {
        Files.writeString(Paths.get(SSD_NAND_FILE), DEFAULT_DATA.repeat(MAX_DATA_COUNT));
    }
}
