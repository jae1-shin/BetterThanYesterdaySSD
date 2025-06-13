package command;

public class ReadCommand extends Command {
    public ReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValidArguments(String[] args) {
        if (args.length != 2) {
            logger.error("ERROR Invalid argument numbers. Usage: read <address>");
            return false;
        }

        if(!isValidAddress(args[1])){
            return false;
        }

        return true;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        int address = Integer.parseInt(args[1]);
        String result = service.read(address);
        if(result.startsWith("ERROR")) {
            logger.result(result);
            return false;
        }
        logger.result("[Read] LBA " + String.format("%02d", address) + " : " + result);

        return true;
    }

    private boolean isValidAddress(String address) {
        try {
            int lba = Integer.parseInt(address);
            if (lba < 0 || lba > 99) {
                logger.result("ERROR LBA must be between 0 and 99.");
                return false;
            }
        }
        catch (NumberFormatException e) {
            logger.error("ERROR NumberFormatException" + e.getMessage());
            return false;
        }

        return true;
    }
}