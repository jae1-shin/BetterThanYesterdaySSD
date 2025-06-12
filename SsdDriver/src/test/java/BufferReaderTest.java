import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;


import static org.junit.jupiter.api.Assertions.*;

class BufferReaderTest {
    private final String bufferPath = "buffer";
    private final BufferReader reader = new BufferReader();

    @BeforeEach
    void setup() throws Exception {
        createFile("1_W_10_0xAAAA_test.txt");
        createFile("2_E_10_2_test.txt");
        createFile("3_W_11_0xBBBB_test.txt");
    }

    void createFile(String fileName) throws Exception {
        File file = new File(bufferPath, fileName);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
        }
    }

    @Test
    void testReadLBA() {
        assertEquals("0x00000000", reader.read(10));
        assertEquals("0xBBBB", reader.read(11));
        assertEquals("", reader.read(12));
    }

    @AfterEach
    void cleanup() {
        File dir = new File(bufferPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

}