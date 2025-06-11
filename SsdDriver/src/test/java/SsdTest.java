import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class SsdTest {

    @BeforeEach
    void setUp() {
        new File("ssd_output.txt").delete();
    }

    @Test
    void 파라미터_부족시_에러_출력() throws Exception {
        Ssd.main(new String[]{"W"});
        BufferedReader br = new BufferedReader(new FileReader("ssd_output.txt"));
        String result = br.readLine();
        br.close();
        Assertions.assertThat(result).isEqualTo("ERROR");
    }

    @Test
    void 잘못된_명령시_에러_출력() throws Exception {
        Ssd.main(new String[]{"X", "1"});
        BufferedReader br = new BufferedReader(new FileReader("ssd_output.txt"));
        String result = br.readLine();
        br.close();
        Assertions.assertThat(result).isEqualTo("ERROR");
    }

    @Test
    void LBA_범위_초과시_에러_출력() throws Exception {
        Ssd.main(new String[]{"W", "100", "0x12345678"});
        BufferedReader br = new BufferedReader(new FileReader("ssd_output.txt"));
        String result = br.readLine();
        br.close();
        Assertions.assertThat(result).isEqualTo("ERROR");
    }

    @Test
    void 데이터_형식_오류시_에러_출력() throws Exception {
        Ssd.main(new String[]{"W", "1", "12345678"});
        BufferedReader br = new BufferedReader(new FileReader("ssd_output.txt"));
        String result = br.readLine();
        br.close();
        Assertions.assertThat(result).isEqualTo("ERROR");
    }
}