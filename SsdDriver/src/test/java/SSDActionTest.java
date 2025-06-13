import command.context.EraseCommandContext;
import command.context.FlushCommandContext;
import command.context.WriteCommandContext;
import command.impl.EraseCommand;
import command.impl.FlushCommand;
import command.impl.ReadCommand;
import command.impl.WriteCommand;
import common.SSDConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SSDActionTest {
    public static final String SAMPLE_DATA = "0xAAAABBBB0xCCCCDDDD0xEEEEFFFF";
    public static final String DEFAULT_VALUE = "0x00000000";
    public static final String WRITE_TEST_VALUE = "0x1234ABCD";
    public static final int TEST_LBA_ADDRESS = 3;

    ReadCommand readCommand = new ReadCommand();
    WriteCommand writeCommand = new WriteCommand();
    EraseCommand eraseCommand = new EraseCommand();

    @BeforeEach
    void setUp() {
        SSD ssd = new SSD();
        try {
            ssd.initFiles();
            deleteBufferFolder();
            writeDefaultValue();
        } catch (IOException e) {
            fail("Initialization failed: " + e.getMessage());
        }
    }

    @Test
    void 다섯개_write_직후_세개_일부_erase_ssdEraserTest_erase() {
        try {
            for (int i = 0; i < 5; i++) {
                writeCommand.execute(new WriteCommandContext(i, WRITE_TEST_VALUE));
                readCommand.read(i);
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(WRITE_TEST_VALUE);
            }

            eraseCommand.execute(new EraseCommandContext(1, 3));
            for (int i : new int[]{1, 2, 3}) {
                readCommand.read(i);
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(SSDConstants.DEFAULT_DATA);
            }

            for (int i : new int[]{0, 4}) {
                readCommand.read(i);
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(WRITE_TEST_VALUE);
            }

        } catch (IOException e) {
            fail("Erase operation failed: " + e.getMessage());
        }

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void 파라미터에_해당하는_데이터_읽기_성공(int LBA) throws IOException {
        writeSampleData();
        readCommand.read(LBA);

        int start = LBA * 10;
        int end = Math.min(start + 10, SAMPLE_DATA.length());
        String expected = SAMPLE_DATA.substring(start, end);

        String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 50, 99})
    void 기록이_한적이_없는_LBA를_읽으면_0X00000000으로_읽힌다(int LBA) throws IOException {
        writeSampleData();
        readCommand.read(LBA);

        String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Disabled
    void 파일_Write시_파일이_없으면_초기화() throws Exception {
        // arrange
        WriteCommand ssdWriteCommand = new WriteCommand();

        // act
        ssdWriteCommand.execute(new WriteCommandContext(TEST_LBA_ADDRESS, WRITE_TEST_VALUE));
        RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r");
        raf.seek(0);
        byte[] buf = new byte[SSDConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf)).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void 파일_Write_정상_동작() throws Exception {
        // arrange

        // act
        writeCommand.execute(new WriteCommandContext(TEST_LBA_ADDRESS, WRITE_TEST_VALUE));
        RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r");
        raf.seek(3 * SSDConstants.BLOCK_SIZE);
        byte[] buf = new byte[SSDConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf)).isEqualTo(WRITE_TEST_VALUE);
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

        FlushCommand flushCommand = new FlushCommand();
        flushCommand.execute(new FlushCommandContext());
        RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r");
        raf.seek(1 * SSDConstants.BLOCK_SIZE);
        byte[] buf = new byte[SSDConstants.BLOCK_SIZE];
        raf.readFully(buf);
        assertThat(new String(buf)).isEqualTo(WRITE_TEST_VALUE);

        raf.seek(2 * SSDConstants.BLOCK_SIZE);
        raf.readFully(buf);
        assertThat(new String(buf)).isEqualTo(DEFAULT_VALUE);

        raf.seek(3 * SSDConstants.BLOCK_SIZE);
        raf.readFully(buf);
        assertThat(new String(buf)).isEqualTo(WRITE_TEST_VALUE);

        raf.seek(4 * SSDConstants.BLOCK_SIZE);
        raf.readFully(buf);
        assertThat(new String(buf)).isEqualTo(DEFAULT_VALUE);

        raf.seek(5 * SSDConstants.BLOCK_SIZE);
        raf.readFully(buf);
        raf.close();
        assertThat(new String(buf)).isEqualTo(WRITE_TEST_VALUE);
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

        FlushCommand flushCommand = new FlushCommand();
        flushCommand.execute(new FlushCommandContext());
        RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r");
        raf.seek(5 * SSDConstants.BLOCK_SIZE);
        byte[] buf = new byte[SSDConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        assertThat(new String(buf)).isEqualTo(DEFAULT_VALUE);
    }

    private void writeDefaultValue() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_VALUE.repeat(100));
        Files.writeString(Paths.get(SSDConstants.SSD_NAND_FILE), sb.toString());
    }

    private void writeSampleData() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "rw")) {
            raf.seek(0);
            raf.write(SAMPLE_DATA.getBytes());
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
}