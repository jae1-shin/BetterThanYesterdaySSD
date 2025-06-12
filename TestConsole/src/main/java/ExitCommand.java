public class ExitCommand extends Command{
    protected ExitCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        throw new RuntimeException("아직 구현 안됐어요");
    }
}