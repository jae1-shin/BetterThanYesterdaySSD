import java.io.IOException;
import java.util.*;

public class SsdFlush implements SsdCommand {

    @Override
    public void execute(Command command) throws IOException {
        flush();
    }

    public void flush() throws IOException {
        List<Command> commandList = BufferUtil.getCommandList();
        for (Command cmd : commandList) {
            if (cmd.type == CommandType.WRITE) {
                SsdWriter writer = new SsdWriter();
                writer.write(cmd.lba, cmd.data);
            } else if (cmd.type == CommandType.ERASE) {
                SsdEraser eraser = new SsdEraser();
                eraser.erase(cmd.lba, cmd.size);
            }
        }

        BufferUtil.clearBuffer();
    }
}
