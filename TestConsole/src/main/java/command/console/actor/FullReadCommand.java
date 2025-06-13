package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.FullReadCommandValidator;

public class FullReadCommand extends Command {
    public static final int EXPECTED_ARGUMENT_COUNT = 1;

    public FullReadCommand(ConsoleService service) {
        super(service);
        validator = new FullReadCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.fullRead();
        return CommandResult.PASS;
    }
}