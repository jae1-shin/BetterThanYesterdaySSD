import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;

import static org.assertj.core.api.Assertions.*;

class MainTest {

    @BeforeEach
    void setUp() {
        new File(SsdConstants.OUTPUT_FILE_PATH).delete();
    }

    @Test
    void 파라미터_부족시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W"});
        BufferedReader br = new BufferedReader(new FileReader(SsdConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();
        
        // assert
        assertThat(result).isEqualTo(SsdConstants.ERROR);
    }

    @Test
    void 잘못된_명령시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"X", "1"});
        BufferedReader br = new BufferedReader(new FileReader(SsdConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SsdConstants.ERROR);
    }

    @Test
    void LBA_범위_초과시_에러_출력() throws Exception {
        // arrange
        
        // act
        Main.main(new String[]{"W", "100", "0x12345678"});
        BufferedReader br = new BufferedReader(new FileReader(SsdConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SsdConstants.ERROR);
    }

    @Test
    void 데이터_형식_오류시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W", "1", "12345678"});
        BufferedReader br = new BufferedReader(new FileReader(SsdConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SsdConstants.ERROR);
    }

    @Test
    void 첫번째_인자가_W인경우_ssd_nand파일에_데이터가_입력된다() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W", "3", "0x1234ABCD"});
        RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "r");
        raf.seek(3 * SsdConstants.BLOCK_SIZE);
        byte[] buf = new byte[SsdConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf)).isEqualTo("0x1234ABCD");
    }

    @Test
    void 첫번째_인자가_R인경우_output파일에_데이터가_입력된다() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"R", "0"});
        RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "r");
        raf.seek(0 * SsdConstants.BLOCK_SIZE);
        byte[] buf = new byte[SsdConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf)).isEqualTo("0xAAAABBBB");
    }

}