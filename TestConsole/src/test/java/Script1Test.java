import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Script1Test {
    @Mock
    private TestConsole testConsole;


    @Test
    void script1_write_readCompare_실행횟수_정상() {
        Script1 script1 = new Script1(testConsole);
        String commandStr = "";

        doReturn(true).when(testConsole).readCompare(anyInt(), anyString());

        script1.execute(commandStr);

        verify(testConsole, times(100)).write(intThat(i -> i >= 0 && i < 100), anyString());
        verify(testConsole, times(100)).readCompare(intThat(i -> i >= 0 && i < 100), anyString());
    }

}