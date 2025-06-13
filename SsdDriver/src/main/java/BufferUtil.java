import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
            String commandFullName = String.join("_", Arrays.copyOfRange(parts, 1, parts.length));

            try {
                int order = Integer.parseInt(parts[0]);
                String cmdType = parts[1];
                int lba = Integer.parseInt(parts[2]);

                if (cmdType.equals("W")) {
                    String data = parts[3];
                    commandList.add(new Command(order, CommandType.WRITE, lba, 1, data, commandFullName));
                } else if (cmdType.equals("E")) {
                    int size = Integer.parseInt(parts[3]);
                    commandList.add(new Command(order, CommandType.ERASE, lba, size, null, commandFullName));
                }
            } catch (Exception e) {
                // ignore
            }

        }
        return commandList;
    }

    public static Command getCommandFromSsdArgs(String[] parts) throws Exception {
        String commandFullName = String.join("_", Arrays.copyOfRange(parts, 0, parts.length));
        String cmdType = parts[0];
        if (cmdType.equals("W")) {
            int lba = Integer.parseInt(parts[1]);
            String data = parts[2];
            return new Command(0, CommandType.WRITE, lba, 1, data, commandFullName);
        } else if (cmdType.equals("R")) {
            int lba = Integer.parseInt(parts[1]);
            return new Command(0, CommandType.READ, lba, 1, null, commandFullName);
        } else if (cmdType.equals("E")) {
            int lba = Integer.parseInt(parts[1]);
            int size = Integer.parseInt(parts[2]);
            return new Command(0, CommandType.ERASE, lba, size, null, commandFullName);
        } else if (cmdType.equals("F")) {
            return new Command(0, CommandType.FLUSH, 0, 0, null, commandFullName);
        }
        return new Command(0, CommandType.EMPTY, 0, 0, null, null);
    }
}
