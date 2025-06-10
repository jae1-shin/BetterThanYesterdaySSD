import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


class SsdReaderTest {

    @BeforeEach
    void setUp() throws IOException {
        Files.writeString(Paths.get("ssd_nand.txt"), "0XAAAABBBB");
    }

    @Test
    void 파라미터_0_99_사이일때_정상작동하고_출력파일에_10글자_기록됨() throws IOException {
        SsdReader reader = new SsdReader();

        reader.read("0");

        String output = null;
        output = Files.readString(Paths.get("ssd_output.txt"));
        assertThat(output).isEqualTo("0XAAAABBBB");
    }

    @Test
    void 파라미터_0_99_아닌경우_ERROR_기록() throws IOException {
        SsdReader reader = new SsdReader();

        reader.read("100");

        String output = Files.readString(Paths.get("ssd_output.txt"));
        assertThat(output).contains("ERROR");
    }

}