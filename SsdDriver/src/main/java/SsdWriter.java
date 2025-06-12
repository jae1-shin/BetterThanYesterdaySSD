import java.io.*;

public class SsdWriter {

    public void write(int address, String data) throws IOException {
        writeData(address, data);
    }

    private void writeData(long address, String data) throws IOException {
        File file = new File(SsdConstants.SSD_NAND_FILE);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(address * SsdConstants.BLOCK_SIZE);
        raf.writeBytes(data);
        raf.close();
    }
}
