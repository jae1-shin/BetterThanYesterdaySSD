package command.validator;

public class ArgsValidator {
    public static final String WRITE_COMMAND = "W";
    public static final String READ_COMMAND = "R";
    public static final String ERASE_COMMAND = "E";
    public static final String FLUSH_COMMAND = "F";

    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    public static final int LBA_MIN = 0;
    public static final int LBA_MAX = 99;

    public static final int ARGUMENT_COMMAND_INDEX = 0;
    public static final int ARGUMENT_ADDRESS_INDEX = 1;
    public static final int ARGUMENT_DATA_INDEX = 2;
    public static final int ARGUMENT_MAX_COUNT = 3;


    public static boolean checkPreCondition(String[] args) {
        if (!isValidArgumentCount(args)) return false;
        if (!isValidCommand(args[ARGUMENT_COMMAND_INDEX])) return false;
        if (!isFlushCommand(args) && !isValidAddressRange(args[ARGUMENT_ADDRESS_INDEX])) return false;
        if (!isValidDataForWrite(args)) return false;
        if (!isValidDataForErase(args)) return false;

        return true;
    }

    private static boolean isValidArgumentCount(String[] args) {
        if (isFlushCommand(args)) {
            return args.length == 1; // Flush command should have only one argument
        }
        return !(args.length < ARGUMENT_DATA_INDEX || args.length > ARGUMENT_MAX_COUNT);
    }

    private static boolean isValidDataForWrite(String[] args) {
        if (isReadCommand(args) || isEraseCommand(args) || isFlushCommand(args)) return true;
        return isWriteCommand(args) && args[ARGUMENT_DATA_INDEX].matches(DATA_FORMAT);
    }

    private static boolean isValidDataForErase(String[] args) {
        if (isReadCommand(args) || isWriteCommand(args) || isFlushCommand(args)) return true;
        if (Integer.parseInt(args[ARGUMENT_DATA_INDEX]) < 0 || Integer.parseInt(args[ARGUMENT_DATA_INDEX]) > 10) return false;
        int lastLBA = Integer.parseInt(args[ARGUMENT_ADDRESS_INDEX]) + Integer.parseInt(args[ARGUMENT_DATA_INDEX]) - 1;
        return isEraseCommand(args) && isValidAddressRange(Integer.toString(lastLBA));
    }

    private static boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < LBA_MIN || LBA > LBA_MAX) {
            return false;
        }
        return true;
    }

    private static boolean isValidCommand(String command) {
        return WRITE_COMMAND.equals(command) || READ_COMMAND.equals(command) || ERASE_COMMAND.equals(command) || FLUSH_COMMAND.equals(command);
    }

    private static boolean isWriteCommand(String[] args) {
        return WRITE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private static boolean isReadCommand(String[] args) {
        return READ_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private static boolean isEraseCommand(String[] args) {
        return ERASE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private static boolean isFlushCommand(String[] args) {
        return FLUSH_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

}
