package command;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValid(String[] args) {
        throw new RuntimeException("아직 구현 안됐어요");
    }

    @Override
    public boolean doExecute(String[] args) {
        throw new RuntimeException("아직 구현 안됐어요");
    }
}