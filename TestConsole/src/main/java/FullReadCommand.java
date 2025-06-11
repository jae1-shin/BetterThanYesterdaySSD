public class FullReadCommand extends Command{
    protected FullReadCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        if(args.length != 1){
            System.out.println("INVALID ARGUMENT");
            return;
        }

        service.fullRead();
    }
}