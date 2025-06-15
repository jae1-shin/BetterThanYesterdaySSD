package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.WriteCommandValidator;

public class WriteCommand extends Command {
    public WriteCommand(ConsoleService service) {
        super(service);
        validator = new WriteCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        if(!service.write(Integer.parseInt(args[1]), args[2])) {
            return CommandResult.error("ERROR : Write failed");
        }
        return CommandResult.pass("[Write] Done");
    }
}
