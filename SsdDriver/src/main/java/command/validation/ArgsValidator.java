package command.validation;

public class ArgsValidator {
    public static final int LBA_MIN = 0;
    public static final int LBA_MAX = 99;
    public static final int ARGUMENT_ADDRESS_INDEX = 1;

    public static boolean isValidAddressRange(String address) {
        try {
            int LBA = Integer.parseInt(address);
            return LBA >= LBA_MIN && LBA <= LBA_MAX;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
