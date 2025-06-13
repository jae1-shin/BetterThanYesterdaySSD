package command;

public class FullReadCommand extends Command {
    public FullReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            return CommandResult.error("ERROR Invalid argument numbers. Usage: read <address>");
        }

        service.fullRead();
        return CommandResult.PASS;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}