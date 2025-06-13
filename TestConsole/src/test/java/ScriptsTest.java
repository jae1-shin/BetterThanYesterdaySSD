import command.common.ConsoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import command.script.Script1;
import command.script.Script2;
import command.script.Script3;
import command.script.Script4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptsTest {
    @Mock
    private ConsoleService service;

    @Test
    void script4_실행횟수_정상() {
        Script4 script4 = new Script4(service);
        int ERASE_CALL_COUNT = 146;
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        script4.execute(new String[]{});

        verify(service, times(script4.LOOP_COUNT* ERASE_CALL_COUNT)).erase(anyInt(), anyInt());
    }

    @Test
    void script4_fail_처리_정상() {
        Script4 script4 = new Script4(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        script4.execute(new String[]{});
        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        Script3 script3 = new Script3(service);
        for (int lba : script3.targetLBA) {
            doReturn(true).when(service).readCompare(eq(lba), anyString());
        }

        script3.execute(new String[]{});

        for (int lba : script3.targetLBA) {
            verify(service, times(script3.LOOP_COUNT)).write(eq(lba), anyString());
            verify(service, times(script3.LOOP_COUNT)).readCompare(eq(lba), anyString());

        }
    }

    @Test
    void script3_fail_처리_정상() {
        Script3 script3 = new Script3(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        script3.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script2_write_readCompare_실행횟수_정상() {
        Script2 script2 = new Script2(service);
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        script2.execute(new String[]{});

        for (int LBA : script2.LBA_TEST_SEQUENCE) {
            verify(service, times(script2.LOOP_COUNT)).write(LBA, script2.TEST_VALUE);
            verify(service, times(script2.LOOP_COUNT)).readCompare(LBA, script2.TEST_VALUE);
        }
    }

    @Test
    void script2_fail_처리_정상() {
        Script2 script2 = new Script2(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        script2.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script1_write_readCompare_실행횟수_정상() {
        Script1 script1 = new Script1(service);
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        script1.execute(new String[]{});

        for (int i = 0; i < script1.LAST_LBA; i++) {
            verify(service, times(1)).write(i, script1.TEST_VALUE);
            verify(service, times(1)).readCompare(i, script1.TEST_VALUE);
        }
    }

    @Test
    void script1_fail_처리_정상() {
        Script1 script1 = new Script1(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        script1.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }
}