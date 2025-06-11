public class WriteCommand extends Command{
    protected WriteCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args)  {
        throw new RuntimeException("아직 구현 안됐어요");
    }
}
