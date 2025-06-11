import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Script3Test {
    @Mock
    private ConsoleService consoleService;

    @Mock
    private ReadCompare readCompare;

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script1 = new Script3(consoleService, readCompare);
        String commandStr = "";

        doReturn(true).when(readCompare).execute(anyInt(), anyString());

        script1.execute(commandStr);

        verify(consoleService, times(200)).write(eq(0), anyString());
        verify(consoleService, times(200)).write(eq(99), anyString());

        verify(readCompare, times(200)).execute(eq(0), anyString());
        verify(readCompare, times(200)).execute(eq(99), anyString());

    }

}