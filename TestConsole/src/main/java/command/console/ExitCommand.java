package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
    }
    public static final int EXPECTED_ARGUMENT_COUNT = 1;

    @Override
    public String isValidArguments(String[] args) {
        if (!isValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) {
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        return CommandResult.EXIT;
    }
}