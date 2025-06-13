package command.validation.impl;

import command.validation.ArgsValidator;
import command.validation.CommandValidator;

public class ReadCommandValidator implements CommandValidator {
    public static final String READ_COMMAND = "R";

    @Override
    public boolean validate(String[] args) {
        return READ_COMMAND.equals(args[0]) &&
                args.length == 2 &&
                ArgsValidator.isValidAddressRange(args[ArgsValidator.ARGUMENT_ADDRESS_INDEX]);
    }
}
