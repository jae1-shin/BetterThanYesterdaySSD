package command.validation;

import command.validation.impl.EraseCommandValidator;
import command.validation.impl.FlushCommandValidator;
import command.validation.impl.ReadCommandValidator;
import command.validation.impl.WriteCommandValidator;

public class CommandValidatorFactory {
    public static CommandValidator getValidator(String command) {
        return switch (command) {
            case "W" -> new WriteCommandValidator();
            case "R" -> new ReadCommandValidator();
            case "E" -> new EraseCommandValidator();
            case "F" -> new FlushCommandValidator();
            default -> null;
        };
    }
}
