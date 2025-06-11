public class FullWriteCommand extends Command{
    protected FullWriteCommand(ConsoleService service) {
        super(service);
    }
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";

    @Override
    public void execute(String[] args) {
        if(args.length != 2){
            System.out.println("[ERROR] INVALID NUMBER OF ARGUMENT");
            return;
        }

        if(isValidData(args[1])){
            System.out.println("[ERROR] INVALID ARGUMENT");
        }
        service.fullWrite(args[1]);
    }

    private boolean isValidData(String data) {
        return data.matches(DATA_FORMAT);
    }
}