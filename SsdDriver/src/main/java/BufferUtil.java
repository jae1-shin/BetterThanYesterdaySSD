import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

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
    public static void clearBuffer() {
        try {
            deleteBufferDirAndFiles();

            File bufferDir = checkAndCreateBufferDir();
            checkAndCreateEmptyBufferFiles(bufferDir);
        } catch (Exception e) {
            // ignore
        }
    }

    public static void deleteBufferDirAndFiles() {
        Path folder = Paths.get(SsdConstants.BUFFER_PATH);
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try (Stream<Path> walk = Files.walk(folder, FileVisitOption.FOLLOW_LINKS)) {
                walk.sorted(Comparator.reverseOrder())
                        .filter(path -> !path.equals(folder))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                // ignore
                            }

                        });
            } catch (IOException e) {
                // ignore
            }
        }

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
            Files.writeString(Paths.get(bufferDir.getPath(), getBufferDefaultFileName(bufferNum)), "");
        }
    }

    private static boolean existBufferFile(File bufferDir, int bufferNum) {
        final String bufferPrefix = getBufferFilePrefix(bufferNum);
        File[] bufferFiles = bufferDir.listFiles((dir, name) -> name.startsWith(bufferPrefix));

        return bufferFiles != null && bufferFiles.length > 0;
    }

    private static String getBufferFilePrefix(int bufferNum) {
        return bufferNum + "_";
    }

    private static String getBufferDefaultFileName(int bufferNum) {
        return getBufferFilePrefix(bufferNum) + "empty";
    }
}
