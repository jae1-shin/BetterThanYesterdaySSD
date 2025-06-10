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

    @Mock
    ReadCompare readCompare;


    @Test
    void ReadCompare_PASS_테스트() {
        int LBA = 1;
        String value = "0x12345678";
        doReturn(true).when(readCompare).execute(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = readCompare.execute(LBA, value);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        int LBA = 1;
        String value = "0x12345678";
        doReturn(true).when(readCompare).execute(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = readCompare.execute(LBA, value);
        assertThat(result).isEqualTo(true);
    }






}