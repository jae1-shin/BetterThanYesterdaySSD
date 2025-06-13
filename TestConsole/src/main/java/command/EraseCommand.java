package command;

public class EraseCommand extends Command {
    public static final int EXPECTED_ARGUMENT_COUNT = 3;

    public EraseCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String isValidArguments(String[] args) {
        if (!isValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) {
            System.out.println("ERROR Invalid argument numbers. ");
            return false;
        }

        if(!isValidAddress(args[1])){
            return false;
        }

        return true;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return true;
    }


}
