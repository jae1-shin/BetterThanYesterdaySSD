package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.EraseCommandValidator;

public class EraseCommand extends Command {
    public EraseCommand(ConsoleService service) {
        super(service);
        validator = new EraseCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return CommandResult.PASS;
    }
}
