package command;

public class EraseRangeCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 3;

    public EraseRangeCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValidArguments(String[] args) {
        if (!isValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) {
            System.out.println("ERROR Invalid argument numbers. ");
            System.out.println("Usage: erase <LBA> <SIZE> or erase_range <Start LBA> <End LBA>");
            return false;
        }

        if(!isValidAddress(args[1]) || !isValidAddress(args[2])){
            return false;
        }

        int start_lba = Integer.parseInt(args[1]);
        if (start_lba < 0 || start_lba > 99) {
            System.out.println("ERROR Start LBA must be between 0 and 99.");
            return false;
        }

        int end_lba = Integer.parseInt(args[2]);
        if (end_lba < 0 || end_lba > 99) {
            System.out.println("ERROR End LBA must be between 0 and 99.");
            return false;
        }

        return true;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return true;
    }

}
