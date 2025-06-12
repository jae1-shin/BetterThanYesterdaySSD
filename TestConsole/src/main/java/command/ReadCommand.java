package command;

public class ReadCommand extends Command {
    public ReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) {
                logger.error("ERROR Invalid argument numbers. Usage: read <address>");
                return false;
            }

            int address = Integer.parseInt(args[1]);
            String result = service.read(address);
            if(result.startsWith("ERROR")) {
                System.out.println(result);
                return false;
            }
            logger.result("[Read] LBA " + String.format("%02d", address) + " : " + result);
        } catch (NumberFormatException e) {
            logger.error("ERROR NumberFormatException" + e.getMessage());
            return false;
        } catch (IndexOutOfBoundsException e) {
            logger.error("ERROR IndexOutOfBoundsException" + e.getMessage());
            return false;
        }

        return true;
    }
}