import command.common.ConsoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import command.script.FullWriteAndReadCompare;
import command.script.PartialLBAWrite;
import command.script.WriteReadAging;
import command.script.EraseAndWriteAging;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptsTest {
    @Mock
    private ConsoleService service;

    @Test
    void script4_실행횟수_정상() {
        EraseAndWriteAging eraseAndWriteAging = new EraseAndWriteAging(service);
        int ERASE_CALL_COUNT = 146;
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        eraseAndWriteAging.execute(new String[]{});

        verify(service, times(eraseAndWriteAging.LOOP_COUNT* ERASE_CALL_COUNT)).erase(anyInt(), anyInt());
    }

    @Test
    void script4_fail_처리_정상() {
        EraseAndWriteAging eraseAndWriteAging = new EraseAndWriteAging(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        eraseAndWriteAging.execute(new String[]{});
        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script3_write_readCompare_실행횟수_정상() {
        WriteReadAging writeReadAging = new WriteReadAging(service);
        for (int lba : writeReadAging.targetLBA) {
            doReturn(true).when(service).readCompare(eq(lba), anyString());
        }

        writeReadAging.execute(new String[]{});

        for (int lba : writeReadAging.targetLBA) {
            verify(service, times(writeReadAging.LOOP_COUNT)).write(eq(lba), anyString());
            verify(service, times(writeReadAging.LOOP_COUNT)).readCompare(eq(lba), anyString());

        }
    }

    @Test
    void script3_fail_처리_정상() {
        WriteReadAging writeReadAging = new WriteReadAging(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        writeReadAging.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script2_write_readCompare_실행횟수_정상() {
        PartialLBAWrite partialLBAWrite = new PartialLBAWrite(service);
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        partialLBAWrite.execute(new String[]{});

        for (int LBA : partialLBAWrite.LBA_TEST_SEQUENCE) {
            verify(service, times(partialLBAWrite.LOOP_COUNT)).write(LBA, partialLBAWrite.TEST_VALUE);
            verify(service, times(partialLBAWrite.LOOP_COUNT)).readCompare(LBA, partialLBAWrite.TEST_VALUE);
        }
    }

    @Test
    void script2_fail_처리_정상() {
        PartialLBAWrite partialLBAWrite = new PartialLBAWrite(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        partialLBAWrite.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }

    @Test
    void script1_write_readCompare_실행횟수_정상() {
        FullWriteAndReadCompare fullWriteAndReadCompare = new FullWriteAndReadCompare(service);
        doReturn(true).when(service).readCompare(anyInt(), anyString());

        fullWriteAndReadCompare.execute(new String[]{});

        for (int i = 0; i < fullWriteAndReadCompare.LAST_LBA; i++) {
            verify(service, times(1)).write(i, fullWriteAndReadCompare.TEST_VALUE);
            verify(service, times(1)).readCompare(i, fullWriteAndReadCompare.TEST_VALUE);
        }
    }

    @Test
    void script1_fail_처리_정상() {
        FullWriteAndReadCompare fullWriteAndReadCompare = new FullWriteAndReadCompare(service);
        doReturn(false).when(service).readCompare(anyInt(), anyString());
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        fullWriteAndReadCompare.execute(new String[]{});

        assertTrue(outContent.toString().trim().equals("FAIL"));
    }
}