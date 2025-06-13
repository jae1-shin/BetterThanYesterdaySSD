package command;

public class EraseRangeCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 3;

    public EraseRangeCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String isValidArguments(String[] args) {
        if (!isValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) {
            return INVALID_ARGUMENT_NUMBER_MSG;
        }

        if(!isValidAddress(args[1]) || !isValidAddress(args[2])){
            return INVALID_ADDRESS_FORMAT_MSG;
        }

        int start_lba = Integer.parseInt(args[1]);
        if (start_lba < 0 || start_lba > 99) {
            return "ERROR Start LBA must be between 0 and 99.";
        }

        int end_lba = Integer.parseInt(args[2]);
        if (end_lba < 0 || end_lba > 99) {
            return "ERROR Start LBA must be between 0 and 99.";
        }

        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return CommandResult.PASS;
    }

}
