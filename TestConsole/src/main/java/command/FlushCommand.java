package command;

public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        service.flush();
        return true;
    }
}
