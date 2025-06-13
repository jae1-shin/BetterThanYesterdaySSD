package command;

public class WriteCommand extends Command {
    public WriteCommand(ConsoleService service) {
        super(service);
    }
    public static final int EXCEPTED_ARGUMENT_COUNT = 3;

    @Override
    public String isValidArguments(String[] args) {
        if(!isValidArgumentCount(args, EXCEPTED_ARGUMENT_COUNT)){
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        if(!isValidAddress(args[1])){
            return INVALID_ADDRESS_FORMAT_MSG;
        }

        if(!isValidData(args[2])){
            return INVALID_DATA_FORMAT;
        }
        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        if(service.write(Integer.parseInt(args[1]), args[2]) == false) {
            logger.error("ERROR Write failed");
            return CommandResult.error("ERROR");
        }else{
            logger.result("[Write] Done");
        }

        return CommandResult.PASS;
    }
}
