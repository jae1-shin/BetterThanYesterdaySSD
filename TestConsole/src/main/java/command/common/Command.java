package command.common;

import logger.Logger;

public abstract class Command implements ICommand {
    public static final String ERROR_FLAG = "ERROR";
    protected Logger logger = Logger.getInstance();
    protected final ConsoleService service;

    public static final String NO_NEED_TO_VALID_CHECK = "";

    public static final String VALID_ARGUMENT = "";
    public static final String INVALID_ARGUMENT_NUMBER_MSG = "Invalid Argument Number !";
    public static final String INVALID_ADDRESS_FORMAT_MSG = "ERROR : LBA must be between 0 and 99.";
    public static final String INVALID_DATA_FORMAT = "ERROR Value must be in hex format (e.g., 0x1234ABCD)";
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    public Command(ConsoleService service) {
        this.service =  service ;
    }

    public CommandResult execute(String[] args){
        String validCheckResult = isValidArguments(args);
        if(!validCheckResult.isEmpty()){
            return CommandResult.error(validCheckResult);
        }

        return doExecute(args);
    }

    abstract public String isValidArguments(String[] args);
    abstract public CommandResult doExecute(String[] args);


    public boolean isNotValidArgumentCount(String[] args, int expected) {
        return args.length != expected;
    }

    public boolean isNotValidAddress(String address) {
        try {
            int lba = Integer.parseInt(address);
            if (lba < 0 || lba > 99) {
                logger.result(INVALID_ADDRESS_FORMAT_MSG);
                return true;
            }
        }
        catch (NumberFormatException e) {
            logger.error("ERROR : NumberFormatException" + e.getMessage());
            return true;
        }

        return false;
    }

    public boolean isNotValidData(String data) {
        return !data.matches(DATA_FORMAT);
    }

    public boolean isErrorExist(String result){
        return result.startsWith(ERROR_FLAG);
    }
}
