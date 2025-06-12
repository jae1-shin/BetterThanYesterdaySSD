import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class SsdTest {
    @BeforeEach
    void setUp() {
        cleanFiles();
    }

    private void cleanFiles() {
        // Clean up created files and directories before each test
        new File(SsdConstants.SSD_NAND_FILE).delete();
        new File(SsdConstants.OUTPUT_FILE_PATH).delete();
        File bufferDir = new File("buffer");
        if (bufferDir.exists()) {
            for (File file : bufferDir.listFiles()) {
                file.delete();
            }
            bufferDir.delete();
        }
    }

    @Test
    void initFiles() {
        Ssd ssd = spy(new Ssd());

        try {
            ssd.initFiles();
        } catch (Exception e) {
            fail("Initialization failed: " + e.getMessage());
        }

        // Check if the SSD NAND file exists
        assertTrue(new File(SsdConstants.SSD_NAND_FILE).exists(), "SSD NAND file should exist");

        // Check if the output file is created
        assertTrue(new File(SsdConstants.OUTPUT_FILE_PATH).exists(), "Output file should exist");

        // Check if buffer directory is created
        assertTrue(new File("buffer").exists(), "Buffer directory should exist");

        // Check if buffer files are created
        for (int bufferNum = 1; bufferNum <= SsdConstants.BUFFER_SIZE; bufferNum++) {
            String bufferPrefix = bufferNum + "_";
            File[] bufferFiles = new File("buffer").listFiles((dir, name) -> name.startsWith(bufferPrefix));
            assertNotNull(bufferFiles, "Buffer files should not be null");
            assertTrue(bufferFiles.length > 0, "Buffer files should exist for buffer number: " + bufferNum);
        }
    }

    @Test
    void SsdEraserTest와_같은_테스트를_processCommand로_진행() {
        SsdReader reader = new SsdReader();
        SsdWriter writer = new SsdWriter();
        Ssd ssd = new Ssd();

        try {
            String expected = "0x12345678";
            for (int i = 0; i < 5; i++) {
                writer.write(i, expected);
                reader.read(i);
                String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(expected);
            }

            ssd.processCommand(new String[]{"E", "1", "3"});
            for (int i : new int[]{1, 2, 3}) {
                reader.read(i);
                String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(SsdConstants.DEFAULT_DATA);
            }

            for (int i : new int[]{0, 4}) {
                reader.read(i);
                String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(expected);
            }

        } catch (IOException e) {
            fail("Erase operation failed: " + e.getMessage());
        }
    }

    @Test
    void processCommand_삭제_유효성검사_걸러내는지_확인() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "0", "10"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

            ssd.processCommand(new String[]{"E", "0", "-1"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 음수개 못 지움

            ssd.processCommand(new String[]{"E", "99", "0"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

            ssd.processCommand(new String[]{"E", "99", "1"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

            ssd.processCommand(new String[]{"E", "91", "20"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 10개 초과 못 지움

            ssd.processCommand(new String[]{"E", "95", "10"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 99 LBA 넘어서 못 자움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}