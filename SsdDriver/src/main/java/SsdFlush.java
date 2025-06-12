import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SsdFlush {

    public void plush() throws IOException {
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

        initBuffer();
    }

    private void initBuffer() throws IOException {
        Path folder = Paths.get("buffer");
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
            }

            try {
                Files.createFile(folder.resolve("1_empty"));
                Files.createFile(folder.resolve("2_empty"));
                Files.createFile(folder.resolve("3_empty"));
                Files.createFile(folder.resolve("4_empty"));
                Files.createFile(folder.resolve("5_empty"));
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
