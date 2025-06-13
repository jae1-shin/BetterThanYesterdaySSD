package command;

public class EraseCommand extends Command {
    public EraseCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValid(String[] args) {
        if (args.length != 3) {
            System.out.println("ERROR Invalid argument numbers. ");
            System.out.println("Usage: erase <LBA> <SIZE> or erase_range <Start LBA> <End LBA>");
            return false;
        }

        int lba = Integer.parseInt(args[1]);
        if (lba < 0 || lba > 99) {
            System.out.println("ERROR LBA must be between 0 and 99.");
            return false;
        }

        return true;
    }

    @Override
    public boolean doExecute(String[] args) {
        service.erase(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return true;
    }

}
