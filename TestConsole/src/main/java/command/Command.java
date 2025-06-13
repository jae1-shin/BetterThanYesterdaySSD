package command;

import logger.Logger;

public abstract class Command implements ICommand {
    protected Logger logger = Logger.getInstance();
    protected final ConsoleService service;

    public Command(ConsoleService service) {
        this.service =  service ;
    }

    public CommandResult execute(String[] args){
        if(!isValidArguments(args)){
            logger.error("Invalid Arguments");
        }

        return doExecute(args);
    }

    abstract public boolean isValidArguments(String[] args);

    abstract public CommandResult doExecute(String[] args);


    public boolean isValidArgumentCount(String[] args, int expected) {
        return args.length != expected;
    }

    public boolean isValidAddress(String address) {
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
