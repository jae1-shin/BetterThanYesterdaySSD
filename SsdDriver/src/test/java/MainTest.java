import command.impl.Reader;
import command.impl.Writer;
import common.SSDConstants;
import common.util.BufferUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class MainTest {

    @BeforeEach
    void setUp() {
        new File(SSDConstants.SSD_NAND_FILE).delete();
        new File(SSDConstants.OUTPUT_FILE_PATH).delete();
        BufferUtil.initBuffer();
    }
        
    @Test
    void 잘못된_명령시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"X", "1"});
        BufferedReader br = new BufferedReader(new FileReader(SSDConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SSDConstants.ERROR);
    }

    @Test
    void 쓰기_파라미터_부족시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W"});
        BufferedReader br = new BufferedReader(new FileReader(SSDConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();
        
        // assert
        assertThat(result).isEqualTo(SSDConstants.ERROR);
    }

    @Test
    void 쓰기_LBA_범위_초과시_에러_출력() throws Exception {
        // arrange
        
        // act
        Main.main(new String[]{"W", "100", "0x12345678"});
        BufferedReader br = new BufferedReader(new FileReader(SSDConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SSDConstants.ERROR);
    }

    @Test
    void 쓰기_데이터_형식_오류시_에러_출력() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W", "1", "12345678"});
        BufferedReader br = new BufferedReader(new FileReader(SSDConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo(SSDConstants.ERROR);
    }


    @Test
    void 삭제_args_유효성검사_정상1() {
        try {
            Main.main(new String[]{"E", "0", "10"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_정상2() {
        try {
            Main.main(new String[]{"E", "99", "0"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_정상3() {
        try {
            Main.main(new String[]{"E", "99", "1"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_음수개() {
        try {
            Main.main(new String[]{"E", "0", "-1"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SSDConstants.ERROR); // 음수개 못 지움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_10개_초과() {
        try {
            Main.main(new String[]{"E", "91", "20"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SSDConstants.ERROR); // 10개 초과 못 지움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 삭제_args_유효성검사_비정상_범위_초과() {
        try {
            Main.main(new String[]{"E", "95", "10"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            assertThat(output).isEqualTo(SSDConstants.ERROR); // 99 LBA 넘어서 못 자움

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void flush_정상_동작_output_단순_검증() {
        try {
            Main.main(new String[]{"F"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            
            assertThat(output).isEqualTo("");
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void flush_정상_동작_buffer_파일_검증() {
        try {
            Main.main(new String[]{"W", "0", "0x12345678"});
            Main.main(new String[]{"E", "1", "2"});
            
            Main.main(new String[]{"F"});
            
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
            
            assertThat(output).isEqualTo("");
            assertThat(BufferUtil.getCommandList()).satisfiesAnyOf(
                    list -> assertThat(list).isNull(),
                    list -> assertThat(list).isEmpty()
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: read
    @Test
    void 읽기_args_정상_테스트() {
        try {
            Main.main(new String[]{"R", "0"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));

            assertThat(output).isEqualTo(SSDConstants.DEFAULT_DATA);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 읽기_args_비정상_LBA_범위_테스트() {
        try {
            Main.main(new String[]{"R", "100"}); // LBA 범위 문제
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));

            assertThat(output).isEqualTo(SSDConstants.ERROR);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 읽기_args_비정상_인자_불충분_테스트() {
        try {
            Main.main(new String[]{"R"}); // 인자 불충분
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));

            assertThat(output).isEqualTo(SSDConstants.ERROR);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 읽기_args_비정상_인자_과다_테스트() {
        try {
            Main.main(new String[]{"R", "1", "1"}); // 인자 과다
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));

            assertThat(output).isEqualTo(SSDConstants.ERROR);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 읽기_정상_동작_파일_테스트() {
        try {
            Main.main(new String[]{"W", "1", "0x12345678"});
            Main.main(new String[]{"R", "1"});
            String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));

            assertThat(output).isEqualTo("0x12345678");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Disabled // 더이상 직접 바로 쓰지 않음 - 버퍼에 기록
    void 첫번째_인자가_W인경우_ssd_nand파일에_데이터가_입력된다() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W", "3", "0x1234ABCD"});
        RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r");
        raf.seek(3 * SSDConstants.BLOCK_SIZE);
        byte[] buf = new byte[SSDConstants.BLOCK_SIZE];
        raf.readFully(buf);
        raf.close();

        // assert
        assertThat(new String(buf)).isEqualTo("0x1234ABCD");
    }

    @Test
    void 첫번째_인자가_R인경우_output파일에_데이터가_입력된다() throws Exception {
        // arrange

        // act
        Main.main(new String[]{"W", "3", "0x1234ABCD"});
        Main.main(new String[]{"R", "3"});
        BufferedReader br = new BufferedReader(new FileReader(SSDConstants.OUTPUT_FILE_PATH));
        String result = br.readLine();
        br.close();

        // assert
        assertThat(result).isEqualTo("0x1234ABCD");
    }

    @Test
    void 다섯개_write_직후_세개_일부_erase_ssd_processCommand() {
        Reader reader = new Reader();
        Writer writer = new Writer();
        SSD ssd = new SSD();

        try {
            String expected = "0x12345678";
            for (int i = 0; i < 5; i++) {
                writer.write(i, expected);
                reader.read(i);
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(expected);
            }

            ssd.processCommand(new String[]{"E", "1", "3"});
            for (int i : new int[]{1, 2, 3}) {
                ssd.processCommand(new String[]{"R", String.valueOf(i)});
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(SSDConstants.DEFAULT_DATA);
            }

            for (int i : new int[]{0, 4}) {
                ssd.processCommand(new String[]{"R", String.valueOf(i)});
                String output = Files.readString(Paths.get(SSDConstants.OUTPUT_FILE_PATH));
                assertThat(output).isEqualTo(expected);
            }

        } catch (IOException e) {
            fail("Erase operation failed: " + e.getMessage());
        }
    }

}