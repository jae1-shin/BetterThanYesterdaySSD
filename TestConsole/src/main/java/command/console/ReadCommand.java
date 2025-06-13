package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class ReadCommand extends Command {
    public ReadCommand(ConsoleService service) {
        super(service);
    }
    public static final int EXCEPTED_ARGUMENT_COUNT = 2;

    @Override
    public String isValidArguments(String[] args) {
        if(isNotValidArgumentCount(args, EXCEPTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        if(isNotValidAddress(args[1])) return INVALID_ADDRESS_FORMAT_MSG;
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