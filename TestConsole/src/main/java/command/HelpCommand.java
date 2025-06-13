package command;

public class HelpCommand extends Command {
    public HelpCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String isValidArguments(String[] args) {
        if(!isValidArgumentCount(args, 1)){
            return INVALID_ARGUMENT_NUMBER_MSG;
        }
        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        //
        // service.help(); 헬프가없네요! 누락된거같은데 수정부탁드립니다!
        return CommandResult.PASS;
    }

}