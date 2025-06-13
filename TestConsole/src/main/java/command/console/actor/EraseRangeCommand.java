package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.EraseRangeCommandValidator;

public class EraseRangeCommand extends Command {
    public EraseRangeCommand(ConsoleService service) {
        super(service);
        validator = new EraseRangeCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return CommandResult.PASS;
    }
}
