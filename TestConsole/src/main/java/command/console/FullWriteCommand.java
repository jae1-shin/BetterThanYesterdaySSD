package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class FullWriteCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 2;

    public FullWriteCommand(ConsoleService service) {
        super(service);
    }

    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(isInValidData(args[1])) return INVALID_DATA_FORMAT;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.fullWrite(args[1]);
        return CommandResult.PASS;
    }
}
