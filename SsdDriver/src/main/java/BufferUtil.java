import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void clearBuffer() {
        try {
            deleteBufferDirAndFiles();

            File bufferDir = checkAndCreateBufferDir();
            checkAndCreateEmptyBufferFiles(bufferDir);
        } catch (Exception e) {
            // ignore
        }
    }

    private static void deleteBufferDirAndFiles() {
        File bufferDir = new File(SsdConstants.BUFFER_PATH);
        bufferDir.delete();
    }

    public static File checkAndCreateBufferDir() {
        File bufferDir = new File(SsdConstants.BUFFER_PATH);
        if (!bufferDir.exists()) {
            bufferDir.mkdirs();
        }
        return bufferDir;
    }

    public static void checkAndCreateEmptyBufferFiles(File bufferDir) throws IOException {
        for (int bufferNum = 1; bufferNum <= SsdConstants.BUFFER_SIZE; bufferNum++) {
            if (existBufferFile(bufferDir, bufferNum)) continue;
            Files.writeString(Paths.get(bufferDir.getPath(), SsdConstants.getBufferDefaultFileName(bufferNum)), "");
        }
    }

    public static boolean existBufferFile(File bufferDir, int bufferNum) {
        final String bufferPrefix = SsdConstants.getBufferFilePrefix(bufferNum);
        File[] bufferFiles = bufferDir.listFiles((dir, name) -> name.startsWith(bufferPrefix));

        return bufferFiles != null && bufferFiles.length > 0;
    }
}
