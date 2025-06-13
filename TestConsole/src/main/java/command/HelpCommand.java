package command;

public class HelpCommand extends Command {
    public HelpCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValidArguments(String[] args) {
        if(!isValidArgumentNumber(args)){
            logger.error("Invalid number of argument");
            return false;
        }
        return true;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.help();
        return true;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}