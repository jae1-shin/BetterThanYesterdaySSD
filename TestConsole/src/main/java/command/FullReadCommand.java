package command;

public class FullReadCommand extends Command {
    public FullReadCommand(ConsoleService service) {
        super(service);
    }
    
    @Override
    public boolean isValid(String[] args) {
        if(!isValidArgumentNumber(args)){
            logger.error("ERROR Invalid argument numbers. Usage: read <address>");
            return false;
        }
        return true;
    }

    @Override
    public boolean doExecute(String[] args) {
        service.fullRead();
        return true;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }

}