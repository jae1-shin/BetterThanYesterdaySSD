import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WriteCommandTest {

    @Test
    void WRITE에서_익셉션안나면_성공() {

        ConsoleService tc = new ConsoleService();

        assertDoesNotThrow(() -> tc.write(4,"0x12345678"));
    }

    @Test
    void WRITE에서_true_성공() {

        ConsoleService tc = spy(new ConsoleService());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(tc).read(4);

        // 테스트
        boolean result = tc.write(4, "0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_전부_성공인지(){
        ConsoleService cs = spy(new ConsoleService());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(cs).read(anyInt());
        boolean result = cs.fullWrite("0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_fail나는지_확인하기(){
        ConsoleService tc = spy(new ConsoleService());

        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("1").doReturn("0x12345678").when(tc).read(anyInt());
        boolean result = tc.fullWrite("0x12345678");

        // 검증
        assertFalse(result);
    }

}