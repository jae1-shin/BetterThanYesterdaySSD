public class EraseRangeCommand extends Command{
    protected EraseRangeCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args)  {
        try {
            if (InvalidCheck(args)) return false;

            service.erase_range(Integer.parseInt(args[1]), Integer.parseInt(args[2]));

        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormainvoker.register(\"erase\",  new EraseCommand(service));tException" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR IndexOutOfBoundsException" + e.getMessage());
        }

        return false;
    }

    private static boolean InvalidCheck(String[] args) {

        if (args.length != 2) {
            System.out.println("ERROR Invalid argument numbers. ");
            System.out.println("Usage: erase <LBA> <SIZE> or erase_range <Start LBA> <End LBA>");
            return true;
        }

        int start_lba = Integer.parseInt(args[1]);
        if (start_lba < 0 || start_lba > 99) {
            System.out.println("ERROR Start LBA must be between 0 and 99.");
            return true;
        }

        int end_lba = Integer.parseInt(args[2]);
        if (end_lba < 0 || end_lba > 99) {
            System.out.println("ERROR End LBA must be between 0 and 99.");
            return true;
        }

        return false;
    }
}
