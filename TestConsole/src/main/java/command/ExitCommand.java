package command;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult execute(String[] args) {
        return CommandResult.EXIT;
    }
}