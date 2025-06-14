import command.buffer.ReadBufferCommand;
import command.context.ReadCommandContext;
import command.impl.ReadCommand;
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

class ReadBufferCommandTest {
    private ReadBufferCommand reader;

    @BeforeEach
    void setup() throws Exception {
        reader = new ReadBufferCommand();
        writeDefaultValue();

        createFile("1_W_10_0xAAAABBBB");
        createFile("2_E_10_2");
        createFile("3_W_11_0xBBBBAAAA");
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
    void 버퍼에서_먼저_읽기_성공() throws IOException {
        reader.execute(new ReadCommandContext(10));
        String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
        assertEquals("0x00000000", output);

        reader.execute(new ReadCommandContext(11));
        output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
        assertEquals("0xBBBBAAAA", output);
    }

    @Test
    void 버퍼에_없으면_SSD에서_읽기_성공() throws IOException {
        reader.execute(new ReadCommandContext(12));
        String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
        assertEquals(SSDConstants.DEFAULT_DATA, output);
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