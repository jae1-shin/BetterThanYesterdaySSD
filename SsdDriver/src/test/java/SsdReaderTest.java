import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


class SsdReaderTest {

    public static final String READ_FILE_PATH = "ssd_nand.txt";
    public static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    public static final String SAMPLE_DATA = "0XAAAABBBB0XCCCCDDDD0XEEEEFFFF";
    public static final String DEFAULT_VALUE = "0x00000000";

    private SsdReader reader;

    @BeforeEach
    void setUp() throws IOException {
        reader = new SsdReader();
        writeDefaultValue();
        writeSampleData();
    }

    private void writeDefaultValue() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_VALUE.repeat(100));
        Files.writeString(Paths.get(READ_FILE_PATH), sb.toString());
    }

    private void writeSampleData() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(READ_FILE_PATH, "rw")) {
            raf.seek(0);
            raf.write(SAMPLE_DATA.getBytes());
        }
    }

    @Test
    void 파라미터_0_99_사이일때_정상작동하고_출력파일에_10글자_기록됨() throws IOException {
        reader.read("0");

        String output = Files.readString(Paths.get(OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(SAMPLE_DATA.substring(0, 10));
    }

    @Test
    void 파라미터_0_99_아닌경우_ERROR_기록() throws IOException {
        reader.read("100");

        String output = Files.readString(Paths.get(OUTPUT_FILE_PATH));
        assertThat(output).contains("ERROR");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "2"})
    void 파라미터에_해당하는_데이터_읽기_성공(String lbaParam) throws IOException {
        reader.read(lbaParam);

        int index = Integer.parseInt(lbaParam);
        int start = index * 10;
        int end = Math.min(start + 10, SAMPLE_DATA.length());
        String expected = SAMPLE_DATA.substring(start, end);

        String output = Files.readString(Paths.get(OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"5", "50", "99"})
    void 기록이_한적이_없는_LBA를_읽으면_0X00000000으로_읽힌다(String lbaParam) throws IOException {
        reader.read(lbaParam);

        String output = Files.readString(Paths.get(OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(DEFAULT_VALUE);
    }

}