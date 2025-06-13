import command.buffer.BufferReader;
import command.impl.Reader;
import common.SSDConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.junit.jupiter.api.Assertions.*;

class BufferReaderTest {
    private BufferReader reader;

    @BeforeEach
    void setup() throws Exception {
        Reader ssdReader = new Reader();
        reader = new BufferReader(ssdReader);
        writeDefaultValue();

        createFile("1_W_10_0xAAAA_test");
        createFile("2_E_10_2_test");
        createFile("3_W_11_0xBBBB_test");
    }

    void writeDefaultValue() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(SSDConstants.DEFAULT_DATA.repeat(100));
        Files.writeString(Paths.get(SSDConstants.SSD_NAND_FILE), sb.toString());
    }

    void createFile(String fileName) throws Exception {
        File file = new File(SSDConstants.BUFFER_PATH, fileName);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
        }
    }

    @Test
    void 버퍼에서_먼저_읽기_성공() {
        assertEquals("0x00000000", reader.read(10));
        assertEquals("0xBBBB", reader.read(11));
    }

    @Test
    void 버퍼에_없으면_SSD에서_읽기_성공() {
        assertEquals(SSDConstants.DEFAULT_DATA, reader.read(12));
    }

    @AfterEach
    void cleanup() {
        File dir = new File(SSDConstants.BUFFER_PATH);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

}