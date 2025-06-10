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
    void script1_write_실행_정상() {
        Script1 script1 = new Script1(testConsole);
        String commandStr = "";

        script1.execute(commandStr);

        verify(testConsole, times(100)).write(any());
    }

}