import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BufferControllerTest {

    @Test
    void Ignore_command_1번_케이스_검증() {
        String cmd_1 = "W_20_0xABCDABCD";
        String cmd_2 = "W_21_0x12341234";
        String cmd_3 = "W_20_0xEEEEFFFF";

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(2);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("W_21_0x12341234");
        assertThat(bufferController.getBufCmdList().get(1).toString()).isEqualTo("W_20_0xEEEEFFFF");

    }

    @Test
    void Ignore_command_2번_케이스_검증() {
        String cmd_1 = "E_18_3";
        String cmd_2 = "W_21_12341234";
        String cmd_3 = "E_18_5";

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(1);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("E_18_5");

    }

    @Test
    void merge_command_케이스_검증() {
        String cmd_1 = "W_20_0xABCDABCD";
        String cmd_2 = "E_10_4";
        String cmd_3 = "E_12_3";

        BufferController bufferController = new BufferController();
        bufferController.optimizeBuffer();

        assertThat(bufferController.getBufCmdList()).isEqualTo(2);
        assertThat(bufferController.getBufCmdList().get(0).toString()).isEqualTo("W_20_0xABCDABCD");
        assertThat(bufferController.getBufCmdList().get(1).toString()).isEqualTo("E_10_5");

    }
}