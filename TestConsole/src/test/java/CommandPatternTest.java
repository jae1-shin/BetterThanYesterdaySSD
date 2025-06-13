import command.common.*;
import org.junit.jupiter.api.Disabled;
import command.console.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CommandPatternTest {
    ConsoleService mockService = mock(ConsoleService.class);

    @Test
    void ReadCommand호출시_service_read_호출_테스트() {
        // Arrange
        ConsoleService mockService = mock(ConsoleService.class);
        doReturn("0x12345678").when(mockService).read(anyInt());
        ReadCommand command = new ReadCommand(mockService);
        String[] args = {"read", "0"};

        // Act
        command.execute(args);

        // Assert
        verify(mockService).read(0);
    }

    @Test
    void WriteCommand호출시_service_write_호출_테스트() {
        // Arrange
        ConsoleService mockService = mock(ConsoleService.class);
        WriteCommand command = new WriteCommand(mockService);
        String[] args = {"write", "0", "0x12341234"};

        // Act
        command.execute(args);

        // Assert
        verify(mockService).write(Integer.parseInt(args[1]), args[2]);
    }

    @Test
    void FullReadCommand호출시_service_fullRead_호출_테스트() {
        // Arrange
        ConsoleService mockService = mock(ConsoleService.class);
        FullReadCommand command = new FullReadCommand(mockService);
        String[] args = {"fullread"};

        // Act
        command.execute(args);

        // Assert
        verify(mockService).fullRead();
    }

    @Test
    void FullWriteCommand호출시_service_fullWrite_호출_테스트() {
        // Arrange
        ConsoleService mockService = mock(ConsoleService.class);
        FullWriteCommand command = new FullWriteCommand(mockService);
        String[] args = {"fullwrite", "0x12345678"};

        // Act
        command.execute(args);

        // Assert
        verify(mockService).fullWrite("0x12345678");
    }

    @Test
    void HelpCommand호출시_service_help_호출_테스트() {
        // Arrange
        HelpCommand command = mock(HelpCommand.class);
        String[] args = {"help"};

        // Act
        command.execute(args);

        // Assert
        verify(command).execute(any());
    }

}