import java.io.File;
import java.util.Comparator;
import java.util.List;

public class BufferReader {

    public String read(int targetLBA) {
        File bufferDir = new File(SsdConstants.BUFFER_PATH);
        File[] files = bufferDir.listFiles();
        if (files == null) return "";

        List<Command> commandList = BufferUtil.getCommandList(files);

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
        return cmd.type == Command.Type.ERASE &&
                targetLBA >= cmd.lba && targetLBA < (cmd.lba + cmd.size);
    }

    private boolean isTargetLBAWrited(int targetLBA, Command cmd) {
        return cmd.type == Command.Type.WRITE && cmd.lba == targetLBA;
    }

}
