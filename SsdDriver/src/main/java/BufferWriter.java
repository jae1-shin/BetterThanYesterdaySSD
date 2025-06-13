import java.io.IOException;

public class BufferWriter implements SsdCommand{

    @Override
    public void execute(Command command) throws IOException {
        BufferController.getInstance().processCommand(command);
    }
}
