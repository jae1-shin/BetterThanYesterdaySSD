import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ReadCompareTest {
    @Mock
    TestConsole testConsole;


    @Test
    void ReadCompare_PASS_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(true).when(testConsole).readCompare(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = testConsole.readCompare(LBA, value);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(false).when(testConsole).readCompare(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = testConsole.readCompare(LBA, value);
        assertThat(result).isEqualTo(false);
    }






}