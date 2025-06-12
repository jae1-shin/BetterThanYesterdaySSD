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
    }
}
