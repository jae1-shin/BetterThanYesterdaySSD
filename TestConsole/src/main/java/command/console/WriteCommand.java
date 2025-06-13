package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class WriteCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 3;

    public WriteCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(!addressValidCheck(args[1]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        if(isInValidData(args[2])) return INVALID_DATA_FORMAT;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        if(!service.write(Integer.parseInt(args[1]), args[2])) {
            return CommandResult.error("ERROR : Write failed");
        }
        return CommandResult.pass("[Write] Done");
    }
}
