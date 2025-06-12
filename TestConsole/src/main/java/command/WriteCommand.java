package command;

public class WriteCommand extends Command {
    public WriteCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args)  {
        try {
            if (args.length != 3) {
                logger.error("ERROR Invalid argument numbers. Usage: read <address>");
                return false;
            }
            if (!args[2].startsWith("0x")) {
                logger.error("ERROR Value must be in hex format (e.g., 0x1234ABCD)");
                return false;
            }

            if(service.write(Integer.parseInt(args[1]), args[2]) == false) {
                logger.error("ERROR Write failed");
                return false;
            }else{
                logger.result("[Write] Done");
            }


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
