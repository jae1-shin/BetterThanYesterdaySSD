package command;

public class ExitCommand extends Command {
    public ExitCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        throw new RuntimeException("아직 구현 안됐어요");
    }
}