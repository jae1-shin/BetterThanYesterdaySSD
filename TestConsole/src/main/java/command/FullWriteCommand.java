package command;

public class FullWriteCommand extends Command {

    public static final int EXCEPTED_ARGUMENT_COUNT = 2;

    public FullWriteCommand(ConsoleService service) {
        super(service);
    }
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    @Override
    public String isValidArguments(String[] args) {
        if(!isValidArgumentCount(args, EXCEPTED_ARGUMENT_COUNT)){
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        if(!isValidData(args[1])){
            return INVALID_DATA_FORMAT;
        }

        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.fullWrite(args[1]);
        return CommandResult.PASS;
    }

}
