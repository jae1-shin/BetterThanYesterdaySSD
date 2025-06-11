import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandPatternTest {
    ConsoleService mockService = mock(ConsoleService.class);

    @Test
    void ReadCommand호출시_service_read_호출_테스트() {
        // Arrange
        ConsoleService mockService = mock(ConsoleService.class);
        ReadCommand command = new ReadCommand(mockService);
        String[] args = {"read", "0"};

        // Act
        command.execute(args);

        // Assert
        verify(mockService).read(0);
    }
}