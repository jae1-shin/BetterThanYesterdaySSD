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
    public String isValidArguments(String[] args) {
        if (isNotValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) {
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        if(isNotValidAddress(args[1]) || isNotValidAddress(args[2])){
            return INVALID_ADDRESS_FORMAT_MSG;
        }

        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return CommandResult.PASS;
    }

}
