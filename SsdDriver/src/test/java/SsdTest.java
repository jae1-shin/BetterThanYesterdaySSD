import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

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

}