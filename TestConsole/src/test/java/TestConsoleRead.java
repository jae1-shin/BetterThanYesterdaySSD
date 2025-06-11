import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestConsoleRead {

    @Test
    void 읽기_1_LBA_성공() {
        String input = "read 3";
        String expect = "0xAAAABBBB";

        TestConsole mockConsole = mock(TestConsole.class);
        when(mockConsole.read(input)).thenReturn(expect);

        assertThat(mockConsole.read(input)).isEqualTo(expect);
    }

    @Test
    void 읽기_1_LBA_실패() {
        String input = "read 4";
        String expect = "ERROR";

        TestConsole mockConsole = mock(TestConsole.class);
        when(mockConsole.read(input)).thenReturn(expect);

        assertThat(mockConsole.read(input)).startsWith(expect);
    }


}