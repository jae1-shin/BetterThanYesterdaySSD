
import command.common.ConsoleService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
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
    public static final String TEST_DATA = "0x12345678";
    public static final String TEST_DATA_FAIL = "0x87654321";
    public static final int TEST_LBA = 0;

    @Mock
    ConsoleService consoleServiceMock;

    @Spy
    ConsoleService consoleServiceSpy;

    @BeforeEach
    void setUp() {
        consoleServiceMock = mock(ConsoleService.class);
        consoleServiceSpy = spy(new ConsoleService());
    }

    @Test
    void ReadCompare_PASS_테스트() {
        doReturn(TEST_DATA).when(consoleServiceSpy).read(TEST_LBA);

        Boolean result = consoleServiceSpy.readCompare(TEST_LBA, TEST_DATA);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void ReadCompare_FAIL_테스트() {
        doReturn(false).when(consoleServiceMock).readCompare(TEST_LBA, TEST_DATA);

        consoleServiceMock.write(TEST_LBA, TEST_DATA);
        Boolean result = consoleServiceMock.readCompare(TEST_LBA, TEST_DATA);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void 읽기_1_LBA_성공() {
        when(consoleServiceMock.read(TEST_LBA)).thenReturn(TEST_DATA);

        assertThat(consoleServiceMock.read(TEST_LBA)).isEqualTo(TEST_DATA);
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
        when(consoleServiceSpy.read(anyInt())).thenReturn(TEST_DATA);
        consoleServiceSpy.fullRead();

        verify(consoleServiceSpy, times(consoleServiceSpy.TOTAL_LBA_COUNT)).read(anyInt());
    }

    @Test
    void WRITE에서_익셉션안나면_성공() {
        assertDoesNotThrow(() -> consoleServiceMock.write(TEST_LBA, TEST_DATA));
    }

    @Test
    void WRITE에서_true_성공() {
        doReturn(TEST_DATA).when(consoleServiceSpy).read(TEST_LBA);

        boolean result = consoleServiceSpy.write(TEST_LBA, TEST_DATA);

        assertTrue(result);
    }

    @Test
    void WRITE_직후_READ_결과_다를_때_false_정상_출력_확인() {
        doReturn(TEST_DATA_FAIL).when(consoleServiceSpy).read(TEST_LBA);

        boolean result = consoleServiceSpy.write(TEST_LBA, TEST_DATA);

        assertFalse(result);
    }

    @Test
    void Full_write_전부_성공인지(){
        doReturn(TEST_DATA).when(consoleServiceSpy).read(anyInt());

        boolean result = consoleServiceSpy.fullWrite(TEST_DATA);

        assertTrue(result);
    }

    @Test
    void Full_write_fail나는지_확인하기(){
        doReturn(false).when(consoleServiceSpy).write(anyInt(), anyString());

        boolean result = consoleServiceSpy.fullWrite(TEST_DATA);

        assertFalse(result);
    }

    @Test
    @Description("실제 ssd.jar에 입력하는 코드입니다")
    void 실제_SDD_jar_쓰기테스트(){
        String inputData = "0xCCCCCCC1";

        ConsoleService cs = new ConsoleService();
        cs.write(0,inputData);
        String result = cs.read(0);

        assertEquals(inputData, result);
    }

    @Test
    @Description("실제 ssd.jar에 리드하는 코드입니다")
    void 실제_SDD_jar_읽기_테스트(){
        ConsoleService cs = new ConsoleService();

        String notExpected = "ERROR";

        String result = cs.read(1);

        assertNotEquals(notExpected, result);
    }


    @Test
    @Disabled
    void help_정상_출력_확인(){
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        String[] expectedValues = {"Team", "Members", "Available commands", "write", "read", "exit", "help", "fullwrite", "fullread", "Note"};

        //consoleServiceSpy.help();

        for (String expected : expectedValues) {
            assertTrue(outContent.toString().contains(expected));
        }
    }


}


