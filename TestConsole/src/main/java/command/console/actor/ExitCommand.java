package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.ExitCommandValidator;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
        validator = new ExitCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        return CommandResult.EXIT;
    }
}