package command;

public abstract class Command implements ICommand {
    public final ConsoleService service;

    public Command(ConsoleService service) {
        this.service =  service ;
    }
}
