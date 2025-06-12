public class EraseCommand extends Command{
    protected EraseCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args)  {
        try {
            if (InvalidCheck(args)) return;

            service.erase(1, 2);


        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormatException" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR IndexOutOfBoundsException" + e.getMessage());
        }

        return ;
    }

    private static boolean InvalidCheck(String[] args) {

        if (args.length != 2) {
            System.out.println("ERROR Invalid argument numbers. ");
            System.out.println("Usage: erase <LBA> <SIZE> or erase_range <Start LBA> <End LBA>");
            return true;
        }

        int lba = Integer.parseInt(args[1]);
        if (lba < 0 || lba > 99) {
            System.out.println("ERROR LBA must be between 0 and 99.");
            return true;
        }

        return false;
    }
}
