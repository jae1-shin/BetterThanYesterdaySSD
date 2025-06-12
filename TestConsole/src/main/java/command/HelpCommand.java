package command;

public class HelpCommand extends Command {
    public HelpCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        if(!isValidArgumentNumber(args)){
            System.out.println("Invalid number of argument");
            return false;
        }

        service.help();
        return true;
    }

    private static boolean isValidArgumentNumber(String[] args) {
        return args.length == 1;
    }
}