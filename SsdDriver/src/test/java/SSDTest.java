import command.CommandContext;
import command.buffer.BufferReader;
import command.impl.Reader;
import command.impl.Writer;
import command.CommandType;
import common.SSDConstants;
import common.util.BufferUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SSDTest {
    @BeforeEach
    void setUp() {
        cleanFiles();
    }

    private void cleanFiles() {
        new File(SSDConstants.SSD_NAND_FILE).delete();
        new File(SSDConstants.OUTPUT_FILE_PATH).delete();
        BufferUtil.initBuffer();
    }

    @Test
//    @DisplayName("BufferUtil.deleteBufferDirAndFiles() TEST")
    void buffer_폴더_및_하위_파일_있을_때_삭제_기능_확인() {
        try {
            BufferUtil.checkAndCreateBuffer();
            BufferUtil.deleteBufferDirAndFiles();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertFalse(new File(SSDConstants.BUFFER_PATH).exists(), "Buffer directory should NOT exist");
    }

    @Test
//    @DisplayName("SSD.initFiles() + BufferUtil.initBuffer() TEST")
    void initFiles_뒤_파일_정상_초기화_및_생성_확인() {
        try {
            SSD ssd = new SSD();
            ssd.initFiles();
        } catch (Exception e) {
            fail("Initialization failed: " + e.getMessage());
        }

        assertTrue(new File(SSDConstants.SSD_NAND_FILE).exists(), "SSD NAND file should exist");
        assertTrue(new File(SSDConstants.OUTPUT_FILE_PATH).exists(), "Output file should exist");
        assertTrue(new File(SSDConstants.BUFFER_PATH).exists(), "Buffer directory should exist");

        for (int bufferNum = 1; bufferNum <= SSDConstants.BUFFER_SIZE; bufferNum++) {
            String bufferPrefix = bufferNum + "_";
            File[] bufferFiles = new File("buffer").listFiles((dir, name) -> name.startsWith(bufferPrefix));
            assertNotNull(bufferFiles, "Buffer files should not be null");
            assertEquals(1, bufferFiles.length, "Buffer files should exist only 1 each -> buffer number: " + bufferNum);
        }
    }

    @Test
//    @DisplayName("BufferUtil.rewriteBuffer() TEST")
    void rewriteBuffer_정상동작_확인() {
        try {
            SSD ssd = new SSD();
            ssd.initFiles();
        } catch (Exception e) {
            fail("Initialization failed: " + e.getMessage());
        }

        List<CommandContext> commandContexts = Arrays.asList(
                new CommandContext(1, CommandType.WRITE, 0, 1, "0x12345678", "W_0_0x12345678"),
                new CommandContext(2, CommandType.WRITE, 2, 1, "0x87654321", "W_2_0x87654321"),
                new CommandContext(3, CommandType.ERASE, 4, 5, null, "E_4_5")
        );

        BufferUtil.rewriteBuffer(commandContexts);

        File bufferDir = new File(SSDConstants.BUFFER_PATH);
        assertTrue(bufferDir.exists(), "buffer 디렉토리가 존재해야 함");

        File[] files = bufferDir.listFiles();
        assertNotNull(files, "buffer 디렉토리의 파일 배열이 null이면 안됨");
        List<String> fileNames = Arrays.stream(files)
                .map(File::getName)
                .sorted()
                .toList();
        List<String> expectedFileNames = Arrays.asList(
                "1_W_0_0x12345678",
                "2_W_2_0x87654321",
                "3_E_4_5",
                "4_empty",
                "5_empty"
        );
        assertThat(fileNames).containsExactlyElementsOf(expectedFileNames);
    }

    @Test
    void 버퍼에_있는_경우_buffer에서_읽기_성공() throws Exception {
        // Arrange
        Reader ssdReader = mock();
        BufferReader bufferReader = new BufferReader(ssdReader);

        int testLBA = 10;
        String testData = "0x12345678";

        List<CommandContext> commandContexts = Arrays.asList(
                new CommandContext(1, CommandType.WRITE, 0, testLBA, testData, "W_10_0x12345678")
        );
        BufferUtil.rewriteBuffer(commandContexts);

        // Act
        String result = bufferReader.read(testLBA);

        // Assert
        assertEquals(testData, result);
        verify(ssdReader, times(0)).read(testLBA);
    }

    @Test
    void 버퍼에_없는_경우_nand파일에서_읽기_성공() throws Exception {
        // Arrange
        Reader ssdReader = mock();
        BufferReader bufferReader = new BufferReader(ssdReader);

        int testLBA = 10;
        String testData = "0x12345678";

        doReturn(testData).when(ssdReader).read(testLBA);
        BufferUtil.initBuffer();

        // Act
        String result = bufferReader.read(testLBA);

        // Assert
        assertEquals(testData, result);
        verify(ssdReader, times(1)).read(testLBA);
    }

}