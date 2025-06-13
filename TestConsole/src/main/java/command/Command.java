package command;

import logger.Logger;

public abstract class Command implements ICommand {
    protected Logger logger = Logger.getInstance();
    protected final ConsoleService service;

    public Command(ConsoleService service) {
        this.service =  service ;
    }

    public boolean execute(String[] args){
        if(!isValid(args)){
            logger.error("Invalid Arguments");
        }

        return doExecute(args);
    }

    abstract public boolean isValid(String[] args);
    abstract public boolean doExecute(String[] args);


}
