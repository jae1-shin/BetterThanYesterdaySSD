package command.console.validator;

import command.common.CommandValidator;

public class FullWriteCommandValidator extends CommandValidator {
    public static final int EXPECTED_ARGUMENT_COUNT = 2;


    @Override
    public String validCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(isInValidData(args[1])) return INVALID_DATA_FORMAT;
        return VALID_CHECK_PASS;
    }
}