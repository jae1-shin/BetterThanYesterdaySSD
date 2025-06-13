package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.FlushCommandValid;

public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
        validator = new FlushCommandValid();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.flush();
        return CommandResult.PASS;
    }
}
