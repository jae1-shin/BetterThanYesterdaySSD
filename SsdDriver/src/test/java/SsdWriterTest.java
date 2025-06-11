import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.*;

class SsdWriterTest {

    public static final String SSD_NAND_FILE = "ssd_nand.txt";
    public static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    public static final String ERROR = "ERROR";
    private SsdWriter ssdWriter;

    @BeforeEach
    void setUp() {
        new File(SSD_NAND_FILE).delete();
        new File(OUTPUT_FILE_PATH).delete();
        ssdWriter = new SsdWriter();
    }

    @Test
    void 파일_Write시_파일이_없으면_초기화() throws Exception {
        // arrange

        // act
        ssdWriter.write(3, "0x1234ABCD");
        RandomAccessFile raf = new RandomAccessFile(SSD_NAND_FILE, "r");
        raf.seek(0);
        byte[] buf = new byte[10];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf) ).isEqualTo("0x00000000");
    }

    @Test
    void 파일_Write_정상_동작() throws Exception {
        // arrange

        // act
        ssdWriter.write(3, "0x1234ABCD");
        RandomAccessFile raf = new RandomAccessFile(SSD_NAND_FILE, "r");
        raf.seek(3 * 10);
        byte[] buf = new byte[10];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf) ).isEqualTo("0x1234ABCD");
    }

    @Test
    void LBA_범위가_아닌_경우_에러_출력() throws Exception {
        //arrange

        //act
        ssdWriter.write(-1, "0xFFFFFFFF");
        String actual = Files.readString(Paths.get(OUTPUT_FILE_PATH));

        //assert
        assertThat(actual).isEqualTo(ERROR);
    }

    @Test
    void Write_데이터_10글자가_아닌경우_에러_출력() throws Exception {
        //arrange

        //act
        ssdWriter.write(0, "0xFFFFFFFFGGGGGGG");
        String actual = Files.readString(Paths.get(OUTPUT_FILE_PATH));

        //assert
        assertThat(actual).isEqualTo(ERROR);
    }

    @Test
    void Write_데이터_0x_시작_미포함인경우_에러_출력() throws Exception {
        //arrange

        //act
        ssdWriter.write(0, "00ZZFFFFFF");
        String actual = Files.readString(Paths.get(OUTPUT_FILE_PATH));

        //assert
        assertThat(actual).isEqualTo(ERROR);
    }
}