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

    @Mock
    private ReadCompare readCompare;

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script1 = new Script3(testConsole, readCompare);
        String commandStr = "";

        doReturn(true).when(readCompare).execute(any());

        script1.execute(commandStr);

        verify(testConsole, times(400)).write(any());
        verify(readCompare, times(400)).execute(any());
    }

}