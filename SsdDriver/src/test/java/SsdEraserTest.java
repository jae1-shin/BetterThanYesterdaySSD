import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SsdEraserTest {
    @BeforeEach
    void setUp() {
        Ssd ssd = new Ssd();
        try {
            ssd.initFiles();
        } catch (IOException e) {
            fail("Initialization failed: " + e.getMessage());
        }
    }

    @Test
    void erase() {
        SsdReader reader = new SsdReader();
        SsdWriter writer = new SsdWriter();
        SsdEraser eraser = new SsdEraser();

        try {
            String expected = "0x12345678";
            for (int i = 0; i < 5; i++) {
                writer.write(i, expected);
                reader.read(i);
                String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(expected);
            }

            // Erase the data at LBA 0
            eraser.erase(1, 3);
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
}