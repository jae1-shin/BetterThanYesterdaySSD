import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class BufferReader implements SsdCommand {

    @Override
    public void execute(Command command) throws IOException {
        read(command.getLba());
    }

    public String read(int targetLBA) {
        List<Command> commandList = BufferUtil.getCommandList();

        // 최신 명령 부터 확인
        commandList.sort(Comparator.comparingInt((Command c) -> c.order).reversed());

        for (Command cmd : commandList) {
            if (isTargetLBAWrited(targetLBA, cmd)) return cmd.data;
            if (isTargetLBAErased(targetLBA, cmd)) return "0x00000000";
        }

        // 못찾은 경우
        return "";
    }

    private boolean isTargetLBAErased(int targetLBA, Command cmd) {
        return cmd.type == CommandType.ERASE &&
                targetLBA >= cmd.lba && targetLBA < (cmd.lba + cmd.size);
    }

    private boolean isTargetLBAWrited(int targetLBA, Command cmd) {
        return cmd.type == CommandType.WRITE && cmd.lba == targetLBA;
    }
}
