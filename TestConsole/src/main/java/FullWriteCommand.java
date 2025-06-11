public class FullWriteCommand extends Command{
    protected FullWriteCommand(ConsoleService service) {
        super(service);
    }
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    @Override
    public void execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            System.out.println("ERROR Invalid argument numbers. Usage: read <address>");
            return;
        }

        if(!isValidData(args[1])){
            System.out.println("ERROR Value must be in hex format (e.g., 0x1234ABCD)");
        }
        service.fullWrite(args[1]);
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 2;
    }

    private boolean isValidData(String data) {
        return data.matches(DATA_FORMAT);
    }
}