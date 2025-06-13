import java.io.IOException;

public class BufferEraser implements SsdCommand {

    @Override
    public void execute(Command command) throws IOException {
        BufferController.getInstance().processCommand(command);
    }
}
