import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteTest {

    @Test
    void WRITE에서_익셉션안나면_성공() {

        String commandStr = "write 4 0x123456";
        TestConsole tc = new TestConsole();

        assertDoesNotThrow(() -> tc.write(commandStr));
    }

    @Test
    void 파일에서_block_단위_읽어오기(){
        TestConsole tc = new TestConsole();
        try {

            InputStream is = getClass().getClassLoader().getResourceAsStream("test.txt");
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            List<String> result = tc.loadBlocks(content);

            // ✅ 기대 결과 정의
            List<String> expected = List.of(
                    "0x12345678", "0x12345678","0x12345678", "0x12345678");

            // ✅ 검증
            assertEquals(expected, result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}