package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.FullWriteCommandValidator;

public class FullWriteCommand extends Command {
    public FullWriteCommand(ConsoleService service) {
        super(service);
        validator = new FullWriteCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.fullWrite(args[1]);
        return CommandResult.PASS;
    }
}
