import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

public class BufferReader implements SsdCommand {

    private final SsdReader ssdReader;

    public BufferReader(SsdReader ssdReader) {
        this.ssdReader = ssdReader;
    }

    @Override
    public void execute(Command command) {
        read(command.getLba());
    }

    public String read(int targetLBA) {
        List<Command> commandList = BufferUtil.getCommandList();

        // 최신 명령 부터 확인
        commandList.sort(Comparator.comparingInt((Command c) -> c.order).reversed());

        for (Command cmd : commandList) {
            if (isTargetLBAWrited(targetLBA, cmd)) {
                writeOutput(cmd.data);
                return cmd.data;
            }
            if (isTargetLBAErased(targetLBA, cmd)) {
                writeOutput(SsdConstants.DEFAULT_DATA);
                return SsdConstants.DEFAULT_DATA;
            }
        }

        // 못찾은 경우
        String readFromSSD = "";
        try {
            readFromSSD = ssdReader.read(targetLBA);
        } catch (IOException e) {
            // ignore
        }

        return readFromSSD;
    }

    private void writeOutput(String readStr) {
        try {
            Files.writeString(Paths.get(SsdConstants.OUTPUT_FILE_PATH),
                    readStr,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            // ignore
        }
    }

    private boolean isTargetLBAErased(int targetLBA, Command cmd) {
        return cmd.type == CommandType.ERASE &&
                targetLBA >= cmd.lba && targetLBA < (cmd.lba + cmd.size);
    }

    private boolean isTargetLBAWrited(int targetLBA, Command cmd) {
        return cmd.type == CommandType.WRITE && cmd.lba == targetLBA;
    }
}
