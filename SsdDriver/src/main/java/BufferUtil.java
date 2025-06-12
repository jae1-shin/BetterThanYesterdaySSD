import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BufferUtil {

    public static List<Command> getCommandList() {
        File bufferDir = new File(SsdConstants.BUFFER_PATH);
        File[] files = bufferDir.listFiles();
        if (files == null) return Collections.emptyList();

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

}
