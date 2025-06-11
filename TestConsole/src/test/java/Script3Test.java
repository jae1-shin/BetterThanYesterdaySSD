import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Script3Test {
    @Mock
    private TestConsole testConsole;


    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script1 = new Script3(testConsole);
        String commandStr = "";

        doReturn(true).when(testConsole).readCompare(anyInt(), anyString());

        script1.execute(commandStr);

        verify(testConsole, times(200)).write(eq(0), anyString());
        verify(testConsole, times(200)).write(eq(99), anyString());

        verify(testConsole, times(200)).readCompare(eq(0), anyString());
        verify(testConsole, times(200)).readCompare(eq(99), anyString());

    }

}