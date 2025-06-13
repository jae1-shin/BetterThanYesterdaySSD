package command.console.validator;

import command.common.CommandValidator;

public class EraseCommandValidator extends CommandValidator {
    public static final int EXPECTED_ARGUMENT_COUNT = 3;
    public static final String ERROR_RANGE_IS_NOT_INTEGER = "ERROR : Range is not integer";

    @Override
    public String validCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(!addressValidCheck(args[1]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        if(isInValidRange(args[2])) return ERROR_RANGE_IS_NOT_INTEGER;
        return VALID_CHECK_PASS;
    }

    public boolean isInValidRange(String range) {
        try {
            int number = Integer.parseInt(range);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
}
