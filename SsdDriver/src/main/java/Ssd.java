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

    public void processCommand(String[] args) {
        if (!checkPreCondition(args)) {
            writeError();
            return;
        }

        if (isWriteCommand(args)) {
            processWriteCommand(args);
        } else if (isReadCommand(args)) {
            processReadCommand(args);
        }
    }

    private static boolean isReadCommand(String[] args) {
        return READ_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private static boolean isWriteCommand(String[] args) {
        return WRITE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private void processWriteCommand(String[] args) {
        SsdWriter writer = new SsdWriter();

        int LBA = Integer.parseInt(args[ARGUMENT_ADDRESS_INDEX]);
        try {
            writer.write(LBA, args[ARGUMENT_DATA_INDEX]);
        } catch (IOException e) {
            // ignore
        }
    }

    private void processReadCommand(String[] args) {
        SsdReader reader = new SsdReader();

        int LBA = Integer.parseInt(args[ARGUMENT_ADDRESS_INDEX]);
        try {
            reader.read(LBA);
        } catch (IOException e) {
            // ignore
        }
    }

    private boolean checkPreCondition(String[] args) {
        if (!isValidArgumentCount(args)) return false;
        if (!isValidCommand(args[ARGUMENT_COMMAND_INDEX])) return false;
        if (!isValidAddressRange(args[ARGUMENT_ADDRESS_INDEX])) return false;
        if (!isValidDataForWrite(args)) return false;
        return true;
    }

    private boolean isValidDataForWrite(String[] args) {
        return isWriteCommand(args) && args[ARGUMENT_DATA_INDEX].matches(DATA_FORMAT);
    }

    private boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < LBA_MIN || LBA > LBA_MAX) {
            return false;
        }
        return true;
    }

    private boolean isValidCommand(String command) {
        return WRITE_COMMAND.equals(command) || READ_COMMAND.equals(command);
    }

    private boolean isValidArgumentCount(String[] args) {
        return !(args.length < ARGUMENT_DATA_INDEX || args.length > ARGUMENT_MAX_COUNT);
    }

    private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            bw.write(ERROR);
        } catch (IOException e) {
            // ignore
        }
    }
}
