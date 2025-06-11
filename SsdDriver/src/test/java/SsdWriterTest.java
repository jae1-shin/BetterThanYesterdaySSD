import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.*;

class SsdWriterTest {
    @BeforeEach
    void setUp() {
        new File("ssd_nand.txt").delete();
        new File("ssd_output.txt").delete();
    }

    @Test
    void 파일_Write시_파일이_없으면_초기화() throws Exception {
        // arrange
        SsdWriter ssdWriter = new SsdWriter();

        // act
        ssdWriter.write(3, "0x1234ABCD");
        RandomAccessFile raf = new RandomAccessFile("ssd_nand.txt", "r");
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
        SsdWriter ssdWriter = new SsdWriter();

        // act
        ssdWriter.write(3, "0x1234ABCD");
        RandomAccessFile raf = new RandomAccessFile("ssd_nand.txt", "r");
        raf.seek(3 * 10);
        byte[] buf = new byte[10];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf) ).isEqualTo("0x1234ABCD");
    }

    @Test
    void 두번째_파라미터_0_99_아닌경우_실패() throws Exception {
        //arrange
        SsdWriter ssdWriter = new SsdWriter();

        //act
        String actual = ssdWriter.write("-1", "0xFFFFFFFF");

        //assert
        assertThat(actual).isEqualTo("ERROR");
    }

    @Test
    void 세번째_파라미터_10글자가_아닌경우_실패() {
        //arrange
        SsdWriter ssdWriter = new SsdWriter();

        //act
        String actual = ssdWriter.write("0", "0xFFFFFFFFGGGGGGG");

        //assert
        assertThat(actual).isEqualTo("ERROR");
    }

    @Test
    void 세번째_파라미터_0x_시작_미포함_확인_실패() {
        //arrange
        SsdWriter ssdWriter = new SsdWriter();

        //act
        String actual = ssdWriter.write("0", "00ZZFFFFFF");

        //assert
        assertThat(actual).isEqualTo("ERROR");
    }

    @Test
    void 파일이_없을때_데이터_저장() {
        //arrange
        SsdWriter ssdWriter = new SsdWriter();

        //act
        String actual = ssdWriter.write("0", "0xABCDEFFF");

        //assert
        assertThat(Paths.get("ssd_output.txt")).isNotNull();
    }
}