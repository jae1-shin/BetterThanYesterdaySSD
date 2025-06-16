import command.CommandType;
import command.context.EraseCommandContext;
import command.context.WriteCommandContext;
import command.impl.buffer.BufferOptimizer;
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

    Path folder = Paths.get("buffer");
    BufferOptimizer bufferOptimizer = BufferOptimizer.getInstance();

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

        bufferOptimizer.processCommand(new WriteCommandContext(20, "0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(1);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferOptimizer.processCommand(new WriteCommandContext(20, "0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(2);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_20_0xEEEEFFFF");
    }

    @Test
    void IgnoreWrite_중복_없음() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferOptimizer.processCommand(new WriteCommandContext(23, "0xEEEEFFFF"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("W_23_0xEEEEFFFF");
    }

    @Test
    void IgnoreErase_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_E_18_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferOptimizer.processCommand(new EraseCommandContext(18, 5));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(1);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("E_18_5");
    }

    @Test
    void IgnoreErase_중복_없음() throws IOException {
        Files.createFile(folder.resolve("1_E_10_3"));
        Files.createFile(folder.resolve("2_W_21_0x12341234"));

        bufferOptimizer.processCommand(new EraseCommandContext(11, 5));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("E_10_3");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_21_0x12341234");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("E_11_5");
    }

    @Test
    void MergeWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_20_0xABCDABCD"));
        Files.createFile(folder.resolve("2_E_10_4"));

        bufferOptimizer.processCommand(new EraseCommandContext(12, 3));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(2);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("E_10_5");
    }

    @Test
    void IgnoreErase_후_MergeWrite_중복_제거_정상() throws IOException {
        Files.createFile(folder.resolve("1_W_5_0x1234ABCD"));
        Files.createFile(folder.resolve("2_W_7_0xABCDABCD"));
        Files.createFile(folder.resolve("3_E_10_2"));
        Files.createFile(folder.resolve("4_W_12_0xABCDEEEE"));

        bufferOptimizer.processCommand(new EraseCommandContext(12, 3));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(3);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_5_0x1234ABCD");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_7_0xABCDABCD");
        assertThat(bufferOptimizer.getBuffer().get(2).getCommandFullName()).isEqualTo("E_10_5");
    }

    @Test
    void Flush후_MergeErase후_IgnoreWrite() throws IOException {
        Files.createFile(folder.resolve("1_E_10_1"));
        Files.createFile(folder.resolve("2_E_11_1"));
        Files.createFile(folder.resolve("3_E_12_1"));
        Files.createFile(folder.resolve("4_E_13_1"));
        Files.createFile(folder.resolve("5_E_14_1"));

        bufferOptimizer.processCommand(new EraseCommandContext(15, 1));
        bufferOptimizer.processCommand(new EraseCommandContext(14, 2));
        bufferOptimizer.processCommand(new EraseCommandContext(16, 1));
        bufferOptimizer.processCommand(new EraseCommandContext(14, 3));
        bufferOptimizer.processCommand(new WriteCommandContext(14, "W_14_0x12345678"));
        bufferOptimizer.processCommand(new EraseCommandContext(14, 3));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(1);
        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("E_14_3");
    }

    @Test
    void IgnoreWrite후_IgnoreErase() throws IOException {
        Files.createFile(folder.resolve("1_W_10_0x12345678"));

        bufferOptimizer.processCommand(new EraseCommandContext(10, 2));
        bufferOptimizer.processCommand(new WriteCommandContext(11, "0x12345678"));
        bufferOptimizer.processCommand(new WriteCommandContext(10, "0x12345678"));

        assertThat(bufferOptimizer.getBuffer().size()).isEqualTo(2);

        assertThat(bufferOptimizer.getBuffer().get(0).getCommandFullName()).isEqualTo("W_11_0x12345678");
        assertThat(bufferOptimizer.getBuffer().get(1).getCommandFullName()).isEqualTo("W_10_0x12345678");
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