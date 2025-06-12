import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FlushTest {

    void initDefaultData() {
        try {
            Files.writeString(Paths.get(SsdConstants.SSD_NAND_FILE), "0x00000000".repeat(100));
        } catch (IOException e) {
        }
    }

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
        initDefaultData();
        deleteBufferFolder();
    }

    @Test
    void 버퍼에서_읽고_Write_Erase_정상_동작_검증() throws Exception {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_1_0x1234ABCD"));
        Files.createFile(folder.resolve("2_E_2_1"));
        Files.createFile(folder.resolve("3_W_3_0x1234ABCD"));
        Files.createFile(folder.resolve("4_E_4_1"));
        Files.createFile(folder.resolve("5_W_5_0x1234ABCD"));

        Flush flush = new Flush();
        flush.plush();
        RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "r");
        raf.seek(1 * SsdConstants.BLOCK_SIZE);
        byte[] buf = new byte[SsdConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        assertThat(new String(buf)).isEqualTo("0x1234ABCD");
    }

    @Test
    void 버퍼에_empty가_있는_경우_스킵_검증() throws Exception {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_1_0x1234ABCD"));
        Files.createFile(folder.resolve("2_E_2_1"));
        Files.createFile(folder.resolve("3_W_3_0x1234ABCD"));
        Files.createFile(folder.resolve("4_E_4_1"));
        Files.createFile(folder.resolve("5_empty"));

        Flush flush = new Flush();
        flush.plush();
        RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "r");
        raf.seek(5 * SsdConstants.BLOCK_SIZE);
        byte[] buf = new byte[SsdConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        assertThat(new String(buf)).isEqualTo("0x00000000");
    }
}