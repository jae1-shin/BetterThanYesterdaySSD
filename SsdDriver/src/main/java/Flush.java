import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Flush {

    public void plush() throws IOException {
        String folderPath = "buffer";
        List<String> sortedNames;
        try (Stream<Path> paths = Files.list(Paths.get(folderPath))) {
            sortedNames = paths
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .collect(toList());
        }

        SsdWriter writer = new SsdWriter();
        for (String fileName : sortedNames) {
            String[] cmdStr = fileName.split("_");
            if (cmdStr.length != 4) continue;
            if ("W".equals(cmdStr[1])) {
                writer.write(Integer.parseInt(cmdStr[2]), cmdStr[3]);
            } else if ("E".equals(cmdStr[1])) {
                // TODO : Erase command로 변경 필요
                writer.write(Integer.parseInt(cmdStr[2]), "0x00000000");
            }
        }

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
