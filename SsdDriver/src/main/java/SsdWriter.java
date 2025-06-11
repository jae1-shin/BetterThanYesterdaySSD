import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SsdWriter {

    public static final String ERROR = "ERROR";
    public static final String DEFAULT_DATA = "0x00000000";
    public static final String SSD_NAND_FILE = "ssd_nand.txt";
    public static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    public static final int MAX_DATA_COUNT = 100;
    public static final int ADDRESS_MAX_RANGE = MAX_DATA_COUNT - 1;
    public static final int ADDRESS_MIN_RANGE = 0;
    public static final int BLOCK_SIZE = 10;

    public void write(int address, String data) {
        if (!isValidAddress(address)) {
            writeError();
            return;
        }

        if (!isValidData(data)) {
            writeError();
            return;
        }

        try {
            checkFileAndWriteDefaultData();
            writeData(address, data);
        } catch (IOException e) {
            writeError();
        }
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

    private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            bw.write(ERROR);
        } catch (IOException e) {
            // ignore
        }
    }

    private boolean isValidData(String writeData) {
        if (writeData == null) return false;
        return writeData.matches("^0x[0-9A-Fa-f]{8}$");
    }

    private boolean isValidAddress(int address) {
        if (address < ADDRESS_MIN_RANGE || address >= ADDRESS_MAX_RANGE) {
            return false;
        }
        return true;
    }
}
