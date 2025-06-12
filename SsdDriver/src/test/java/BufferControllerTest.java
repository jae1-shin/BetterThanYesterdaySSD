import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BufferControllerTest {

    void deleteBufferFolder() {
        Path folder = Paths.get("buffer");
        try (Stream<Path> walk = Files.walk(folder)) {
            walk.sorted(Comparator.reverseOrder())
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

    @BeforeEach
    void setUp() {
        deleteBufferFolder();
    }

    @Test
    void Ignore_command_1번_케이스_검증() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));
        Files.createFile(folder.resolve("3_W_20_0xEEEEFFFF"));

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(2);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("W_21_0x12341234");
        assertThat(bufferController.getBufCmdList().get(1).toString()).isEqualTo("W_20_0xEEEEFFFF");

    }

    @Test
    void Ignore_command_2번_케이스_검증() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_E_18_3"));
        Files.createFile(folder.resolve("2_W_21_12341234"));
        Files.createFile(folder.resolve("3_E_18_5"));

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(1);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("E_18_5");

    }

    @Test
    void merge_command_케이스_검증() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_E_10_4"));
        Files.createFile(folder.resolve("3_E_12_3"));

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(2);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferController.getBufCmdList().get(1).toString()).isEqualTo("E_10_5");

    }
}