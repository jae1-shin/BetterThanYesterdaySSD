import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptsTest {
    @Mock
    private TestConsole testConsole;

    @Mock
    private ReadCompare readCompare;

    @Spy
    Script2 script2;

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script1 = new Script3(testConsole, readCompare);
        String commandStr = "";

        doReturn(true).when(readCompare).execute(anyInt(), anyString());

        script1.execute(commandStr);

        verify(testConsole, times(200)).write(eq(0), anyString());
        verify(testConsole, times(200)).write(eq(99), anyString());

        verify(readCompare, times(200)).execute(eq(0), anyString());
        verify(readCompare, times(200)).execute(eq(99), anyString());

    }

    @Test
    void Script2_테스트_통과 (){
        script2.execute();
        verify(script2, times(1)).execute();
    }

    @Test
    void script1_write_readCompare_실행횟수_정상() {
        Script1 script1 = new Script1(testConsole, readCompare);
        String commandStr = "";

        doReturn(true).when(readCompare).execute(anyInt(), anyString());

        script1.execute(commandStr);

        verify(testConsole, times(100)).write(intThat(i -> i >= 0 && i < 100), anyString());
        verify(readCompare, times(100)).execute(intThat(i -> i >= 0 && i < 100), anyString());
    }

}