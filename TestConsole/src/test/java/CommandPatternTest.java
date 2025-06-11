import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandPatternTest {
    ConsoleService mockService = mock(ConsoleService.class);

    @Test
    void ReadCommand호출시_service_호출_테스트() {
        ReadCommand readCommand = new ReadCommand(mockService);
        String[] args = {"read", "0x00000"};

        // ReadCommand의 execute 메서드가 ConsoleService의 read 메서드를 호출하는지 확인
        readCommand.execute(args);

        // Mockito를 사용하여 ConsoleService의 read 메서드가 호출되었는지 검증
        verify(mockService).read(0x00000); // 실제 테스트에서는 이 부분을 활성화해야 합니다.

        // 현재는 mock이므로, assertTrue를 사용하여 테스트가 성공적으로 실행되었음을 확인
        assertTrue(true);
    }
}