package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class EraseRangeCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 3;

    public EraseRangeCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(!addressValidCheck(args[1]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        if(!addressValidCheck(args[2]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return CommandResult.PASS;
    }
}
