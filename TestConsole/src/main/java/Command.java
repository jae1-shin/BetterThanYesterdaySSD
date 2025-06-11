public abstract class Command implements ICommand {
    protected final ConsoleService service;

    protected Command(ConsoleService service) {
        this.service =  service ;
    }
}
