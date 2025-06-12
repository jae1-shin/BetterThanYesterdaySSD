import command.ConsoleService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ConsoleServiceTest {

    Random random = new Random();

    @Test
    @Description("실제 ssd.jar 테스트입니다.")
    @Disabled
    void erase_실제ssd_jar_지우기_테스트() {
        ConsoleService cs = new ConsoleService();

        cs.erase(95,100);

        System.out.println("Case 0: erase(-100, 100)");
        cs.erase(-100,100);

        System.out.println("Case 1: erase(3, -100)");
        cs.erase(3, -100);   // expect: erase 0~99

        System.out.println("\nCase 2: erase(90, -100)");
        cs.erase(90, -100);  // expect: erase 0~89

        System.out.println("\nCase 3: erase(90, 100)");
        cs.erase(90, 100);   // expect: erase 90~99

        System.out.println("\nCase 4: erase(80, 11)");
        cs.erase(80, 11);    // expect: erase 80~90

        System.out.println("\nCase 5: erase(5, -2)");
        cs.erase(5, -2);     // expect: erase 3~4

    }

    @Test
    void erase_Mock_기본기능테스트() throws Exception {
        ConsoleService cs = spy(new ConsoleService());

        // excuteErase 메서드 실행 막기
        doNothing().when(cs).excuteErase(anyInt(), anyInt());

        cs.erase(5, -2);

        ArgumentCaptor<Integer> lbaCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> chunkCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(cs, atLeastOnce()).excuteErase(lbaCaptor.capture(), chunkCaptor.capture());

        // 첫 호출 인자 검사
        assertEquals(4, lbaCaptor.getAllValues().get(0));
        assertEquals(2, chunkCaptor.getAllValues().get(0));
    }



    @RepeatedTest(3)
    void erase_랜덤테스트_에러안나면_성공() {
        ConsoleService cs = new ConsoleService();

        int start = random.nextInt(100);               // 0 ~ 99
        int size = random.nextInt(2001) - 1000;        // -1000 ~ +1000

        System.out.println("Testing: erase(" + start + ", " + size + ")");
        cs.erase(start, size);
        System.out.println("----------");

        assertDoesNotThrow(() -> cs.erase(start, size));
    }

    @Test
    void eraseRange_기본기능테스트() throws Exception {
        ConsoleService cs = spy(new ConsoleService());

        doNothing().when(cs).excuteErase(anyInt(), anyInt());

        cs.erase_range(0, 10);  // 0~10 범위 지우기

        ArgumentCaptor<Integer> startCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> chunkCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(cs, times(2)).excuteErase(startCaptor.capture(), chunkCaptor.capture());

        assertEquals(10, startCaptor.getValue());
        assertEquals(1, chunkCaptor.getValue());  // 0~10 이니까 총 11개
    }

    @RepeatedTest(3)
    void eraseRange_랜덤테스트_에러안나면_성공() {
        ConsoleService cs = new ConsoleService();

        int start = random.nextInt(100);               // 0 ~ 99
        int end = random.nextInt(100);          // -1000 ~ +1000

        cs.erase_range(start, end);

        assertDoesNotThrow(() -> cs.erase_range(start, end));
    }

}