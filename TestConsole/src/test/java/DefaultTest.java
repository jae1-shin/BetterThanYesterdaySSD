import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultTest {
    @Test
    void exit_입력시_종료메시지를_출력한다() throws IOException {
        // Arrange
        String input = "exit\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        System.setIn(testIn);
        System.setOut(new PrintStream(testOut, true)); // autoFlush true

        try {
            // Act
            TestConsole app = new TestConsole(System.in); // Scanner는 내부에서 System.in 사용
            app.run();

            // Assert
            String output = testOut.toString().trim();
            assertThat(output).contains("Program terminated"); // ✅ 기대 출력 메시지를 검사
        } finally {
            // 복구
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
