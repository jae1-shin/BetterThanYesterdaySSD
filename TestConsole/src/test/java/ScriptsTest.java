import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptsTest {
    @Mock
    private ConsoleService consoleService;

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script1 = new Script3(consoleService);
        String commandStr = "";

        doReturn(true).when(consoleService).readCompare(anyInt(), anyString());

        script1.execute(commandStr);

        verify(consoleService, times(200)).write(eq(0), anyString());
        verify(consoleService, times(200)).write(eq(99), anyString());

        verify(consoleService, times(200)).readCompare(eq(0), anyString());
        verify(consoleService, times(200)).readCompare(eq(99), anyString());

    }

    @Test
    void script2_write_readCompare_실행횟수_정상() {
        Script2 script2 = new Script2(consoleService);
        String commandStr = "";

        doReturn(true).when(consoleService).readCompare(anyInt(), anyString());

        script2.execute(commandStr);

        verify(consoleService, times(150)).write(intThat(i -> i >= 0 && i < 100), anyString());
        verify(consoleService, times(150)).readCompare(intThat(i -> i >= 0 && i < 100), anyString());
    }

    @Test
    void script1_write_readCompare_실행횟수_정상() {
        Script1 script1 = new Script1(consoleService);
        String commandStr = "";
        doReturn(true).when(consoleService).readCompare(anyInt(), anyString());

        script1.execute(commandStr);

        verify(consoleService, times(100)).write(intThat(i -> i >= 0 && i < 100), anyString());
        verify(consoleService, times(100)).readCompare(intThat(i -> i >= 0 && i < 100), anyString());
    }

}