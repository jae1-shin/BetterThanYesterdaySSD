package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class FlushCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 1;

    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.flush();
        return CommandResult.PASS;
    }
}
