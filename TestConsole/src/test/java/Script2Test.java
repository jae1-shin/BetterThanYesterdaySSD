import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Script2Test {
    @Mock
    TestConsole testConsole;

    @Test
    void script2_write_readCompare_실행횟수_정상() {
        Script2 script2 = new Script2(testConsole);
        String commandStr = "";

        doReturn(true).when(testConsole).readCompare(anyInt(), anyString());

        script2.execute();

        verify(testConsole, times(150)).write(intThat(i -> i >= 0 && i < 100), anyString());
        verify(testConsole, times(150)).readCompare(intThat(i -> i >= 0 && i < 100), anyString());
    }

}