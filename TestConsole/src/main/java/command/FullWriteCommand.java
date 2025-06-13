package command;

public class FullWriteCommand extends Command {
    public FullWriteCommand(ConsoleService service) {
        super(service);
    }
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    @Override
    public boolean isValid(String[] args) {
        if(!isValidArgumentNumber(args)){
            logger.error("ERROR Invalid argument numbers. Usage: fullwrite");
            return false;
        }

        if(!isValidData(args[1])){
            logger.error("ERROR Value must be in hex format (e.g., 0x1234ABCD)");
            return false;
        }

        return true;
    }

    @Override
    public boolean doExecute(String[] args) {
        service.fullWrite(args[1]);
        return true;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 2;
    }
    private boolean isValidData(String data) {
        return data.matches(DATA_FORMAT);
    }
}
