package command;

public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValidArguments(String[] args) {
        return true;
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.flush();
        return CommandResult.PASS;
    }
}
