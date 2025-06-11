import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SsdWriter {

    public static final String ERROR = "ERROR";
    public static final int ADDRESS_MIN_RANGE = 0;
    public static final int ADDRESS_MAX_RANGE = 99;

    public void write(int address, String data) {

        try {
            File file = new File("ssd_nand.txt");
            if (!file.exists()) {
                writeDefaultValue();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(address * 10);
            raf.writeBytes(data);
            raf.close();
        } catch (IOException e) {
            writeError();
        }
    }

    private void writeDefaultValue() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("0x00000000".repeat(100));
        Files.writeString(Paths.get("ssd_nand.txt"), sb.toString());
    }

    static private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_output.txt"))) {
            bw.write(ERROR);
        } catch (IOException e) {
            // ignore
        }
    }

    public String write(String addrStr, String data) {
        if (!isValidAddress(addrStr)) {
            return ERROR;
        }

        if (!isValidData(data)) {
            return ERROR;
        }

        return data;
    }

    private boolean isValidData(String writeData) {
        if (writeData == null) return false;
        return writeData.matches("^0x[0-9A-Fa-f]{8}$");
    }

    private boolean isValidAddress(String addrStr) {
        int addr = -1;
        try {
            addr = Integer.valueOf(addrStr);
        } catch (NumberFormatException e) {
            return false;
        }
        if (addr < ADDRESS_MIN_RANGE || addr >= ADDRESS_MAX_RANGE) {
            return false;
        }
        return true;
    }
}
