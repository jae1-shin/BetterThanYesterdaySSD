import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WriteTest {

    @Test
    void WRITE에서_익셉션안나면_성공() {

        TestConsole tc = new TestConsole();

        assertDoesNotThrow(() -> tc.write(4,"0x12345678"));
    }

    @Test
    void WRITE에서_true_성공() {

        TestConsole tc = spy(new TestConsole());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(tc).read(4);

        // 테스트
        boolean result = tc.write(4, "0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_전부_성공인지(){
        TestConsole tc = spy(new TestConsole());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(tc).read(anyInt());
        boolean result = tc.fullWrite("0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_fail나는지_확인하기(){
        TestConsole tc = spy(new TestConsole());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("1").doReturn("0x12345678").when(tc).read(anyInt());
        boolean result = tc.fullWrite("0x12345678");

        // 검증
        assertFalse(result);
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