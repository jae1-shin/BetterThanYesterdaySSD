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

    public static final String SAMPLE_DATA = "0xAAAABBBB0xCCCCDDDD0xEEEEFFFF";
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
        Files.writeString(Paths.get(SsdConstants.SSD_NAND_FILE), sb.toString());
    }

    private void writeSampleData() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "rw")) {
            raf.seek(0);
            raf.write(SAMPLE_DATA.getBytes());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void 파라미터에_해당하는_데이터_읽기_성공(int LBA) throws IOException {
        reader.read(LBA);

        int start = LBA * 10;
        int end = Math.min(start + 10, SAMPLE_DATA.length());
        String expected = SAMPLE_DATA.substring(start, end);

        String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 50, 99})
    void 기록이_한적이_없는_LBA를_읽으면_0X00000000으로_읽힌다(int LBA) throws IOException {
        reader.read(LBA);

        String output = Files.readString(Paths.get(SsdConstants.OUTPUT_FILE_PATH));
        assertThat(output).isEqualTo(DEFAULT_VALUE);
    }

}