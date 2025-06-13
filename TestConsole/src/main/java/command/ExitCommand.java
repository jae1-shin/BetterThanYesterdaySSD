package command;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public String isValidArguments(String[] args) {
        throw new RuntimeException("아직 구현 안됐어요");
    }

    @Override
    public CommandResult doExecute(String[] args) {
        return CommandResult.EXIT;
    }
}