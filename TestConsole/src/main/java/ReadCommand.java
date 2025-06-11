public class ReadCommand implements ICommand {
    private final ConsoleService service;

    public ReadCommand(ConsoleService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        service.read(Integer.parseInt(args[0]));
    }
}