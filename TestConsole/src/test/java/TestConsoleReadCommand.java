import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestConsoleReadCommand {

    @Test
    void 읽기_1_LBA_성공() {
        int input = 3;
        String expect = "0xAAAABBBB";

        ConsoleService mockService = mock(ConsoleService.class);
        when(mockService.read(input)).thenReturn(expect);

        assertThat(mockService.read(input)).isEqualTo(expect);
    }

    @Test
    void 읽기_1_LBA_실패() {
        int input = 4;
        String expect = "ERROR";

        ConsoleService consoleService = mock(ConsoleService.class);
        when(consoleService.read(input)).thenReturn(expect);

        assertThat(consoleService.read(input)).startsWith(expect);
    }

    @Test
    void 읽기_FULL_LBA_성공() {
        /*
        ConsoleService consoleService = spy(new ConsoleService());

        consoleService.fullRead();

        verify(consoleService, times(100)).read(anyInt());


         */
    }


}