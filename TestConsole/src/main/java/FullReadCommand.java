public class FullReadCommand extends Command{
    protected FullReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            System.out.println("INVALID ARGUMENT");
            return;
        }

        service.fullRead();
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}