public class FullReadCommand extends Command{
    protected FullReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            System.out.println("ERROR Invalid argument numbers. Usage: read <address>");
            return false;
        }

        service.fullRead();
        return true;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}