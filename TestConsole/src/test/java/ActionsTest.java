import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ActionsTest {
    @Mock
    TestConsole testConsole;

    @Test
    void ReadCompare_PASS_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(true).when(testConsole).readCompare(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = testConsole.readCompare(LBA, value);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(false).when(testConsole).readCompare(LBA, value);

        testConsole.write(LBA, value);
        Boolean result = testConsole.readCompare(LBA, value);
        assertThat(result).isEqualTo(false);
    }

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
