package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class ReadCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 2;

    public ReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(!addressValidCheck(args[1]).equals(VALID_ADDRESS)) return INVALID_ADDRESS_FORMAT_MSG;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        int address = Integer.parseInt(args[1]);
        String result = service.read(address);
        if(isErrorExist(result)) {
            return CommandResult.error(result);
        }
        logger.result("[Read] LBA " + String.format("%02d", address) + " : " + result);

        return CommandResult.PASS;
    }

}