public class ReadCommand extends Command{
    protected ReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        try {
            int address = Integer.parseInt(args[1]);
            System.out.println("[Read] LBA " + String.format("%02d", address) + " : " + service.read(address));
        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormatException" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR IndexOutOfBoundsException" + e.getMessage());
        }
    }
}