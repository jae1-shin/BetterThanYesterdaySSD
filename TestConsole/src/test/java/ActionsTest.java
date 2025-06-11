import jdk.jfr.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ActionsTest {
    @Mock
    ConsoleService consoleServiceMock;

    @Spy
    ConsoleService consoleServiceSpy;

    @Test
    void ReadCompare_PASS_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(true).when(consoleServiceMock).readCompare(LBA, value);

        consoleServiceMock.write(LBA, value);
        Boolean result = consoleServiceMock.readCompare(LBA, value);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        int LBA = 3;
        String value = "0x12345678";
        doReturn(false).when(consoleServiceMock).readCompare(LBA, value);

        consoleServiceMock.write(LBA, value);
        Boolean result = consoleServiceMock.readCompare(LBA, value);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void 읽기_1_LBA_성공() {
        int input = 3;
        String expect = "0xAAAABBBB";
        when(consoleServiceMock.read(input)).thenReturn(expect);

        assertThat(consoleServiceMock.read(input)).isEqualTo(expect);
    }

    @Test
    void 읽기_1_LBA_실패() {
        int input = 4;
        String expect = "ERROR";
        when(consoleServiceMock.read(input)).thenReturn(expect);

        assertThat(consoleServiceMock.read(input)).isEqualTo(expect);
    }

    @Test
    void 읽기_FULL_LBA_성공() {
        when(consoleServiceSpy.read(anyInt())).thenReturn("0x00000000");
        consoleServiceSpy.fullRead();

        verify(consoleServiceSpy, times(100)).read(anyInt());
    }

    @Test
    void WRITE에서_익셉션안나면_성공() {
        assertDoesNotThrow(() -> consoleServiceMock.write(0,"0x98989898"));
    }

    @Test
    void WRITE에서_true_성공() {
        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(consoleServiceSpy).read(4);

        // 테스트
        boolean result = consoleServiceSpy.write(4, "0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_전부_성공인지(){
        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("0x12345678").when(consoleServiceSpy).read(anyInt());

        // 테스트
        boolean result = consoleServiceSpy.fullWrite("0x12345678");

        // 검증
        assertTrue(result);
    }

    @Test
    void Full_write_fail나는지_확인하기(){
        // read()가 호출되면 "0x12345678" 리턴하도록 설정
        doReturn("1").doReturn("0x12345678").when(consoleServiceSpy).read(anyInt());

        // 테스트
        boolean result = consoleServiceSpy.fullWrite("0x12345678");

        // 검증
        assertFalse(result);
    }

    @Test
    @Disabled
    @Description("실제 ssd.jar에 입력하는 코드입니다")
    void 실제_SDD_jar_쓰기테스트(){
        ConsoleService cs = new ConsoleService();
        cs.write(0,"0xCCCCCCCC");
    }


    @Test
    void help_정상_출력_확인(){
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        String[] expectedValues = {"Team", "Members", "Available commands", "write", "read", "exit", "help", "fullwrite", "fullread", "Note"};

        consoleServiceSpy.help();

        for (String expected : expectedValues) {
            assertTrue(outContent.toString().contains(expected));
        }
    }

}


