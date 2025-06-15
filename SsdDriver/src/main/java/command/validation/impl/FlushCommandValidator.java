package command.validation.impl;

import command.validation.CommandValidator;

public class FlushCommandValidator implements CommandValidator {
    public static final String FLUSH_COMMAND = "F";

    @Override
    public boolean validate(String[] args) {
        return FLUSH_COMMAND.equals(args[0]) && args.length == 1;
    }
}
