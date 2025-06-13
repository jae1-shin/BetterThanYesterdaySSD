import command.CommandContext;
import command.buffer.BufferOptimizer;
import command.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class BufferOptimizerTest {

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

    @BeforeEach
    void setUp() {
        deleteBufferFolder();
    }

    @Test
    void BufferFull시_Flush_후_명령_추가() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));
        Files.createFile(folder.resolve("3_W_22_0x12341234"));
        Files.createFile(folder.resolve("4_W_23_0x12341234"));
        Files.createFile(folder.resolve("5_W_24_0x12341234"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(1, CommandType.WRITE, 20, 0, "0xEEEEFFFF", "W_20_0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(1);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_제거_정상() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(3, CommandType.WRITE, 20, 0, "0xEEEEFFFF", "W_20_0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(2);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_없음() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(3, CommandType.WRITE, 23, 0, "0xEEEEFFFF", "W_23_0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("W_23_0xEEEEFFFF");
    }

    @Test
    void IgnoreErase_중복_제거_정상() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_E_18_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(3, CommandType.ERASE, 18, 5, null, "E_18_5"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(1);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("E_18_5");
    }

    @Test
    void IgnoreErase_중복_없음() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_E_10_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(3, CommandType.ERASE, 11, 5, null, "E_11_5"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("E_10_3");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("E_11_5");
    }

    @Test
    void MergeWrite_중복_제거_정상() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_E_10_4"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(3, CommandType.ERASE, 12, 3, null, "E_12_3"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(2);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("E_10_5");
    }

    @Test
    void IgnoreErase_후_MergeWrite_중복_제거_정상() throws IOException {
        Path folder = Paths.get("buffer");
        Files.createDirectories(folder);
        Files.createFile(folder.resolve("1_W_5_0x1234ABCD"));
        Files.createFile(folder.resolve("2_W_7_0xABCDABCD"));
        Files.createFile(folder.resolve("3_E_10_2"));
        Files.createFile(folder.resolve("4_W_12_0xABCDEEEE"));

        BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();
        bufferOptimizer.processCommand(new CommandContext(5, CommandType.ERASE, 12, 3, null, "E_12_3"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_5_0x1234ABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_7_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("E_10_5");
    }
}