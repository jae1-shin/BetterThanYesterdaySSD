import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BufferReader {

    private final String BUFFER_PATH = "buffer";

    public String read(int targetLBA) {
        File bufferDir = new File(BUFFER_PATH);
        File[] files = bufferDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) return "";

        List<Command> commandList = new ArrayList<>();

        for (File file : files) {
            String name = file.getName().replace(".txt", "");
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

        // 최신 명령 부터 확인
        commandList.sort(Comparator.comparingInt((Command c) -> c.order).reversed());

        for (Command cmd : commandList) {
            // 찾는 LBA에 쓰여진 경우
            if (cmd.type == Command.Type.WRITE && cmd.lba == targetLBA) {
                return cmd.data;
            }
            
            // 찾는 LBA에 지워진 경우
            if (cmd.type == Command.Type.ERASE &&
                    targetLBA >= cmd.lba && targetLBA < (cmd.lba + cmd.size)) {
                return "0x00000000";
            }
        }

        // 못찾은 경우
        return "";
    }

    static class Command {
        enum Type { WRITE, ERASE }

        int order;     // 파일명 앞 숫자
        Type type;
        int lba;
        int size;      // only for ERASE
        String data;   // only for WRITE

        Command(int order, Type type, int lba, int size, String data) {
            this.order = order;
            this.type = type;
            this.lba = lba;
            this.size = size;
            this.data = data;
        }
    }

}
