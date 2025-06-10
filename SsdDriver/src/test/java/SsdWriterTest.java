import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SsdWriterTest {

    @Test
    void 두번째_파라미터_0_99_아닌경우_실패() {
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

}