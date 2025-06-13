package command.console.validator;

import command.common.CommandValidator;

public class ReadCommandValidator extends CommandValidator {
    public static final int EXPECTED_ARGUMENT_COUNT = 2;

    @Override
    public String validCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(!addressValidCheck(args[1]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        return VALID_CHECK_PASS;
    }
}