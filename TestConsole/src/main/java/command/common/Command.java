package command.common;

import logger.Logger;

public abstract class Command implements ICommand {

    public static final String ERROR_FLAG = "ERROR";
    public static final String PASS_FLAG = "PASS";
    public static final String FAIL_FLAG = "FAIL";

    protected CommandValidator validator;
    protected Logger logger = Logger.getInstance();
    protected final ConsoleService service;

    public Command(ConsoleService service) {
        this.service =  service ;
    }

    public CommandResult execute(String[] args){
        String validCheckResult = argumentsValidCheck(args);
        if(!validCheckResult.isEmpty()){
            return CommandResult.error(validCheckResult);
        }

        return doExecute(args);
    }

    public String argumentsValidCheck(String[] args){
        return validator.validCheck(args);
    }
    abstract public CommandResult doExecute(String[] args);

    public boolean isErrorExist(String result){
        return result.startsWith(ERROR_FLAG);
    }
}
