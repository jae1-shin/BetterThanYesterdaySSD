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

    @Spy
    Script2 script2;

    @Test
    void Script2_테스트_통과 (){
        script2.execute();
        verify(script2, times(1)).execute();
    }

}