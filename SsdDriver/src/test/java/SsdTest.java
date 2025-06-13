import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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
    void buffer_폴더_및_하위_파일_있을_때_삭제_기능_확인() {
        try {
            File bufferDir = BufferUtil.checkAndCreateBufferDir();
            BufferUtil.checkAndCreateEmptyBufferFiles(bufferDir);

            BufferUtil.deleteBufferDirAndFiles();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertFalse(new File("buffer").exists(), "Buffer directory should NOT exist");
    }

    @Test
    void initFiles_뒤_파일_정상_초기화_및_생성_확인() {
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
    void 다섯개_write_직후_세개_일부_erase_ssd_processCommand() {
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
    void 삭제_args_유효성검사_정상1() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "0", "10"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_정상2() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "99", "0"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_정상3() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "99", "1"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_음수개() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "0", "-1"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 음수개 못 지움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_10개_초과() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "91", "20"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 10개 초과 못 지움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_범위_초과() {
        Ssd ssd = new Ssd();
        String output;

        try {
            ssd.processCommand(new String[]{"E", "95", "10"});
            output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SsdConstants.ERROR); // 99 LBA 넘어서 못 자움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void rewriteBuffer_정상동작_확인() {
        Ssd ssd = new Ssd();

        try {
            ssd.initFiles();
        } catch (Exception e) {
            fail("Initialization failed: " + e.getMessage());
        }

        List<Command> commands = Arrays.asList(
                new Command(1, CommandType.WRITE, 0, 1, "0x12345678", "W_0_0x12345678"),
                new Command(2, CommandType.WRITE, 2, 1, "0x87654321", "W_2_0x87654321"),
                new Command(3, CommandType.ERASE, 4, 5, null, "E_4_5")
        );

        BufferUtil.rewriteBuffer(commands);

        File bufferDir = new File(SsdConstants.BUFFER_PATH);
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
//
//  [TODO] 리팩토링(모킹을 위해 SSD 에서 생성자 주입 필요) 후 주석 풀기
//    @Test
//    void 버퍼에서_읽기_성공() throws Exception {
//
//        int testLBA = 10;
//        doReturn("0xABCD").when(bufferReader).read(testLBA);
//
//        Ssd ssd = new Ssd(bufferReader, ssdReader);
//        ssd.processCommand(new String[]{"R", "10"});
//
//        verify(bufferReader).read(testLBA);
//        verify(ssdReader, never()).read(anyInt());
//    }
//
//    @Test
//    void 버퍼에_없는_경우_nand파일에서_읽기_성공() throws Exception {
//        int testLBA = 20;
//        doReturn("").when(bufferReader).read(testLBA);
//
//        Ssd ssd = new Ssd(bufferReader, ssdReader);
//        ssd.processCommand(new String[]{"R", "20"});
//
//        verify(bufferReader).read(testLBA);
//        verify(ssdReader).read(testLBA);
//    }

}