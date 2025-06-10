import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ReadCompareTest {
    @Mock
    Write write;

    @Mock
    ReadCompare readCompare;


    @Test
    void ReadCompare_PASS_테스트() {
        String commandStr = "write 3 0x12345678";
        doReturn(true).when(readCompare).execute(commandStr);

        write.execute(commandStr);
        Boolean result = readCompare.execute(commandStr);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        String commandStr = "write 3 0x12345678";
        doReturn(false).when(readCompare).execute(commandStr);

        write.execute(commandStr);
        Boolean result = readCompare.execute(commandStr);
        assertThat(result).isEqualTo(false);
    }




}