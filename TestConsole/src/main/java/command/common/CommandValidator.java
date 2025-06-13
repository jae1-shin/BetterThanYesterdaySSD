package command.common;

public class CommandValidator implements ICommandValidator{

    public static final String VALID_CHECK_PASS = "";
    public static final String VALID_CHECK_FAIL = "VALID_CHECK_FAIL";
    public static final String VALID_ARGUMENT = "";
    public static final String VALID_ADDRESS = "";
    public static final String VALID_DATA_FORMAT = "";
    public static final String INVALID_ARGUMENT_NUMBER_MSG = "ERROR : Invalid Argument Number !";
    public static final String INVALID_ADDRESS_FORMAT_MSG = "ERROR : LBA must be between 0 and 99.";
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";
    public static final String INVALID_DATA_FORMAT = "ERROR Value must be in hex format (e.g., 0x1234ABCD)";
    public static final String NO_NEED_TO_VALID_CHECK = "";


    @Override
    public String validCheck(String[] args) {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    public boolean isInValidArgumentCount(String[] args, int expected) {
        return args.length != expected;
    }

    public String addressValidCheck(String address) {
        try {
            int lba = Integer.parseInt(address);
            if (lba < 0 || lba > 99) {
                return INVALID_ADDRESS_FORMAT_MSG;
            }
        }
        catch (NumberFormatException e) {
            return "ERROR : NumberFormatException" + e.getMessage();
        }

        return VALID_ADDRESS;
    }

    public boolean isInValidData(String data) {
        return !data.matches(DATA_FORMAT);
    }
}
