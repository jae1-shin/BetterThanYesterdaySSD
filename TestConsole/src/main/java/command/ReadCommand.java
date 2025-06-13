package command;

public class ReadCommand extends Command {
    public ReadCommand(ConsoleService service) {
        super(service);
    }
    public static final int EXCEPTED_ARGUMENT_COUNT = 2;

    @Override
    public String isValidArguments(String[] args) {
        if(!isValidArgumentCount(args, EXCEPTED_ARGUMENT_COUNT)){
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        if(!isValidAddress(args[1])){
            return INVALID_ADDRESS_FORMAT_MSG;
        }

        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        int address = Integer.parseInt(args[1]);
        String result = service.read(address);
        if(result.startsWith("ERROR")) {
            logger.result(result);
            return CommandResult.error("ERROR");
        }
        logger.result("[Read] LBA " + String.format("%02d", address) + " : " + result);

        return CommandResult.PASS;
    }

}