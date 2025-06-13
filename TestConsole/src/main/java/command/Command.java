package command;

import logger.Logger;

public abstract class Command implements ICommand {
    protected Logger logger = Logger.getInstance();
    protected final ConsoleService service;

    public Command(ConsoleService service) {
        this.service =  service ;
    }

}
