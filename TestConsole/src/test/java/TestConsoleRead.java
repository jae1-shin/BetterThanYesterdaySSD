import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestConsoleRead {

    @Test
    void 읽기_1_LBA_성공() {
        int input = 3;
        String expect = "0xAAAABBBB";

        TestConsole mockConsole = mock(TestConsole.class);
        when(mockConsole.read(input)).thenReturn(expect);

        assertThat(mockConsole.read(input)).isEqualTo(expect);
    }

    @Test
    void 읽기_1_LBA_실패() {
        int input = 4;
        String expect = "ERROR";

        TestConsole mockConsole = mock(TestConsole.class);
        when(mockConsole.read(input)).thenReturn(expect);

        assertThat(mockConsole.read(input)).startsWith(expect);
    }

    @Test
    void 읽기_FULL_LBA_성공() {
        TestConsole mockConsole = mock(TestConsole.class);

        mockConsole.fullRead();

        verify(mockConsole, times(100)).read(anyInt());
    }


}