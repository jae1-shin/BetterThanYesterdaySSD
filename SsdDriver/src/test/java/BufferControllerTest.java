import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class BufferControllerTest {
    Path folder = Paths.get("buffer");
    BufferController bufferController = BufferController.getInstance();

    @BeforeEach
    void setUp() throws IOException {
        deleteBufferFolder();
        Files.createDirectories(folder);
    }

    @Test
    void BufferFull시_Flush_후_명령_추가() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));
        Files.createFile(folder.resolve("3_W_22_0x12341234"));
        Files.createFile(folder.resolve("4_W_23_0x12341234"));
        Files.createFile(folder.resolve("5_W_24_0x12341234"));

        bufferController.processCommand(new Command(0, CommandType.WRITE, 20, 0, "0xEEEEFFFF", "W_20_0xEEEEFFFF"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(1);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferController.processCommand(new Command(0, CommandType.WRITE, 20, 0, "0xEEEEFFFF", "W_20_0xEEEEFFFF"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(2);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferController.getBuffer().get(1).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_없음() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferController.processCommand(new Command(0, CommandType.WRITE, 23, 0, "0xEEEEFFFF", "W_23_0xEEEEFFFF"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(3);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferController.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferController.getBuffer().get(2).getCommandFullName()).isEqualTo("W_23_0xEEEEFFFF");
    }

    @Test
    void IgnoreErase_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_E_18_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 18, 5, null, "E_18_5"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(1);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("E_18_5");
    }

    @Test
    void IgnoreErase_중복_없음() throws IOException {
        Files.createFile(folder.resolve("1_E_10_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 11, 5, null, "E_11_5"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(3);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("E_10_3");
        assertThat(bufferController.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferController.getBuffer().get(2).getCommandFullName()).isEqualTo("E_11_5");
    }

    @Test
    void MergeWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_E_10_4"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 12, 3, null, "E_12_3"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(2);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferController.getBuffer().get(1).getCommandFullName()).isEqualTo("E_10_5");
    }

    @Test
    void IgnoreErase_후_MergeWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_5_0x1234ABCD"));
        Files.createFile(folder.resolve("2_W_7_0xABCDABCD"));
        Files.createFile(folder.resolve("3_E_10_2"));
        Files.createFile(folder.resolve("4_W_12_0xABCDEEEE"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 12, 3, null, "E_12_3"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(3);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("W_5_0x1234ABCD");
        assertThat(bufferController.getBuffer().get(1).getCommandFullName()).isEqualTo("W_7_0xABCDABCD");
        assertThat(bufferController.getBuffer().get(2).getCommandFullName()).isEqualTo("E_10_5");
    }

    @Test
    void Flush후_MergeErase후_IgnoreWrite() throws IOException {
        Files.createFile(folder.resolve("1_E_10_1"));
        Files.createFile(folder.resolve("2_E_11_1"));
        Files.createFile(folder.resolve("3_E_12_1"));
        Files.createFile(folder.resolve("4_E_13_1"));
        Files.createFile(folder.resolve("5_E_14_1"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_15_1"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_2"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_16_1"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_3"));
        bufferController.processCommand(new Command(0, CommandType.WRITE, 15, 1, null, "W_14_0x12345678"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_3"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(1);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("E_14_3");
    }

    @Test
    void IgnoreWrite후() throws IOException {
        Files.createFile(folder.resolve("1_W_10_0x12345678"));

        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_15_1"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_2"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_16_1"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_3"));
        bufferController.processCommand(new Command(0, CommandType.WRITE, 15, 1, null, "W_14_0x12345678"));
        bufferController.processCommand(new Command(0, CommandType.ERASE, 15, 1, null, "E_14_3"));

        assertThat(bufferController.getBuffer().size()).isEqualTo(1);
        assertThat(bufferController.getBuffer().get(0).getCommandFullName()).isEqualTo("E_14_3");
    }

    void deleteBufferFolder() {
        Path folder = Paths.get("buffer");
        try (Stream<Path> walk = Files.walk(folder)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            // ignore
                        }
                    });
        } catch (IOException e) {
            // ignore
        }
    }
}