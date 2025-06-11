public class ReadCommand extends Command{
    protected ReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("ERROR Not enough arguments. Usage: read <address>");
                return;
            }

            int address = Integer.parseInt(args[1]);
            String result = service.read(address);
            if(result.startsWith("ERROR")) {
                System.out.println(result);
                return;
            }

            System.out.println("[Read] LBA " + String.format("%02d", address) + " : " + result);
        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormatException" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR IndexOutOfBoundsException" + e.getMessage());
        }
    }
}