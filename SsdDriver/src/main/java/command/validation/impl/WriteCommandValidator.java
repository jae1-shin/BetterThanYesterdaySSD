package command.validation.impl;

import command.validation.ArgsValidator;
import command.validation.CommandValidator;

public class WriteCommandValidator implements CommandValidator {
    public static final String WRITE_COMMAND = "W";
    public static final int ARGUMENT_DATA_INDEX = 2;
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    @Override
    public boolean validate(String[] args) {
        return WRITE_COMMAND.equals(args[0]) &&
                ArgsValidator.isValidArgumentCount(args) &&
                ArgsValidator.isValidAddressRange(args[ArgsValidator.ARGUMENT_ADDRESS_INDEX]) &&
                isValidData(args);
    }

    private boolean isValidData(String[] args) {
        return args[ARGUMENT_DATA_INDEX].matches(DATA_FORMAT);
    }
}
