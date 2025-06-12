import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BufferReader {

    private final String BUFFER_PATH = "buffer";

    public String read(int targetLBA) {
        File bufferDir = new File(BUFFER_PATH);
        File[] files = bufferDir.listFiles();
        if (files == null) return "";

        List<Command> commandList = getCommandList(files);

        // 최신 명령 부터 확인
        commandList.sort(Comparator.comparingInt((Command c) -> c.order).reversed());

        for (Command cmd : commandList) {
            if (isTargetLBAWrited(targetLBA, cmd)) return cmd.data;
            if (isTargetLBAErased(targetLBA, cmd)) return "0x00000000";
        }

        // 못찾은 경우
        return "";
    }

    private static List<Command> getCommandList(File[] files) {
        List<Command> commandList = new ArrayList<>();

        for (File file : files) {
            String name = file.getName();
            if (name.contains("empty")) continue;

            String[] parts = name.split("_");

            try {
                int order = Integer.parseInt(parts[0]);
                String cmdType = parts[1];
                int lba = Integer.parseInt(parts[2]);

                if (cmdType.equals("W")) {
                    String data = parts[3];
                    commandList.add(new Command(order, Command.Type.WRITE, lba, 1, data));
                } else if (cmdType.equals("E")) {
                    int size = Integer.parseInt(parts[3]);
                    commandList.add(new Command(order, Command.Type.ERASE, lba, size, null));
                }
            } catch (Exception e) {
                // ignore
            }

        }
        return commandList;
    }

    private boolean isTargetLBAErased(int targetLBA, Command cmd) {
        return cmd.type == Command.Type.ERASE &&
                targetLBA >= cmd.lba && targetLBA < (cmd.lba + cmd.size);
    }

    private boolean isTargetLBAWrited(int targetLBA, Command cmd) {
        return cmd.type == Command.Type.WRITE && cmd.lba == targetLBA;
    }

    static class Command {
        enum Type { WRITE, ERASE }

        int order;
        Type type;
        int lba;
        int size;
        String data;

        Command(int order, Type type, int lba, int size, String data) {
            this.order = order;
            this.type = type;
            this.lba = lba;
            this.size = size;
            this.data = data;
        }
    }

}
