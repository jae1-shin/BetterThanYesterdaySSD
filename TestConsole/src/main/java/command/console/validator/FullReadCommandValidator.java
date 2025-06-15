package command.console.validator;

import command.common.CommandValidator;

public class FullReadCommandValidator extends CommandValidator {
    public static final int EXPECTED_ARGUMENT_COUNT = 1;

    @Override
    public String validCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        return VALID_CHECK_PASS;
    }
}