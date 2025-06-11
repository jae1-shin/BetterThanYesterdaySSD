public class HelpCommand extends Command{
    protected HelpCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            System.out.println("Invalid number of argument");
            return;
        }

        service.help();
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}