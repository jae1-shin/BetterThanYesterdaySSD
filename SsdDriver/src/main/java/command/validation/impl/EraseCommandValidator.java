package command.validation.impl;

import command.validation.ArgsValidator;
import command.validation.CommandValidator;

public class EraseCommandValidator implements CommandValidator {
    public static final String ERASE_COMMAND = "E";
    public static final int ARGUMENT_ADDRESS_INDEX = 1;
    public static final int ARGUMENT_DATA_INDEX = 2;

    @Override
    public boolean validate(String[] args) {
        return ERASE_COMMAND.equals(args[0]) &&
                args.length == 3 &&
                isValidData(args);
    }

    private boolean isValidData(String[] args) {
        if (Integer.parseInt(args[ARGUMENT_DATA_INDEX]) < 0 || Integer.parseInt(args[ARGUMENT_DATA_INDEX]) > 10) return false;
        try {
            int lastLBA = Integer.parseInt(args[ARGUMENT_ADDRESS_INDEX]) + Integer.parseInt(args[ARGUMENT_DATA_INDEX]) - 1;
            return ArgsValidator.isValidAddressRange(Integer.toString(lastLBA));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
