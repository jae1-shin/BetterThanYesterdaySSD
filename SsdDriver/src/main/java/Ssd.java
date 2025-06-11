import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ssd {

    public static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";
    public static final String WRITE_COMMAND = "W";
    public static final String READ_COMMAND = "R";
    public static final String ERROR = "ERROR";
    public static final int ARGUMENT_COMMAND_INDEX = 0;
    public static final int ARGUMENT_ADDRESS_INDEX = 1;
    public static final int ARGUMENT_DATA_INDEX = 2;
    public static final int LBA_MIN = 0;
    public static final int LBA_MAX = 99;
    public static final int ARGUMENT_MAX_COUNT = 3;

    public static void main(String[] args) {
        if (!isValidArgumentCount(args)) {
            writeError();
            return;
        }

        if (!isValidCommand(args[ARGUMENT_COMMAND_INDEX])) {
            writeError();
            return;
        }

        if (!isValidAddressRange(args[ARGUMENT_ADDRESS_INDEX])) {
            writeError();
            return;
        }

        if (!isValidDataForWrite(args)) {
            writeError();
            return;
        }
    }

    private static boolean isValidDataForWrite(String[] args) {
        return WRITE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]) && args[ARGUMENT_DATA_INDEX].matches(DATA_FORMAT);
    }

    private static boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < LBA_MIN || LBA > LBA_MAX) {
            return false;
        }
        return true;
    }

    private static boolean isValidCommand(String command) {
        return WRITE_COMMAND.equals(command) || READ_COMMAND.equals(command);
    }

    private static boolean isValidArgumentCount(String[] args) {
        return !(args.length < ARGUMENT_DATA_INDEX || args.length > ARGUMENT_MAX_COUNT);
    }

    static private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            bw.write(ERROR);
        } catch (IOException e) {
            // ignore
        }
    }
}
