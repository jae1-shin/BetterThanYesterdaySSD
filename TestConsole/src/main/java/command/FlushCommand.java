package command;

public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult execute(String[] args) {
        service.flush();
        return CommandResult.PASS;
    }
}
