package command;

public class FlushCommand extends Command {
    public FlushCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean isValid(String[] args) {
        return true;
    }

    @Override
    public boolean doExecute(String[] args) {
        service.flush();
        return true;
    }
}
