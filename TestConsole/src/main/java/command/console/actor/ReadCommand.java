package command.console.actor;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;
import command.console.validator.ReadCommandValidator;

public class ReadCommand extends Command {
    public ReadCommand(ConsoleService service) {
        super(service);
        validator = new ReadCommandValidator();
    }

    @Override
    public CommandResult doExecute(String[] args) {
        int address = Integer.parseInt(args[1]);
        String result = service.read(address);
        if(isErrorExist(result)) {
            return CommandResult.error(result);
        }

        return CommandResult.pass("[Read] LBA " + String.format("%02d", address) + " : " + result);
    }

}