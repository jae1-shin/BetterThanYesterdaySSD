public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        return false;
    }
}
