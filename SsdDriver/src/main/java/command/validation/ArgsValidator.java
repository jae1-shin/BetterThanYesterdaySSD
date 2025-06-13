package command.validation;

public class ArgsValidator {
    public static final int LBA_MIN = 0;
    public static final int LBA_MAX = 99;

    public static final int ARGUMENT_ADDRESS_INDEX = 1;
    public static final int ARGUMENT_DATA_INDEX = 2;
    public static final int ARGUMENT_MAX_COUNT = 3;


    public static boolean isValidArgumentCount(String[] args) {
        return !(args.length < ARGUMENT_DATA_INDEX || args.length > ARGUMENT_MAX_COUNT);
    }

    public static boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < LBA_MIN || LBA > LBA_MAX) {
            return false;
        }
        return true;
    }

}
