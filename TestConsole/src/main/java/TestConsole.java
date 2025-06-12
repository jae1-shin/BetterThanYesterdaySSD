
import logger.LoggerContext;

import java.io.InputStream;
import java.util.Scanner;
import static logger.LoggerHolder.logger;

public class TestConsole {

    private final Scanner scanner;

    public TestConsole() {
        this.scanner = new Scanner(System.in);
    }

    public TestConsole(InputStream in) {
        this.scanner = new Scanner(in);
    }

    public void run() {
        // 콘솔엔 안 나옴
        logger.debug("디버그 메시지");
        // 콘솔에 출력됨
        logger.info("정보 메시지");
        // 줄바꿈 없이 콘솔 출력
        logger.result("결과 메시지", false);
        logger.result(" ← 이어짐");
        logger.error("에러 발생!");

        // Console 끄고 파일에만 기록
        logger.result("콘솔 출력 없이 파일만 기록됨", LoggerContext.FILE_ONLY);

        // Console ON + 줄바꿈 OFF
        logger.result("줄바꿈 없이 출력됨", LoggerContext.CONSOLE_NO_NEWLINE);
        logger.result(" ← 이어짐");


        ConsoleService service = new ConsoleService();
        CommandInvoker invoker = new CommandInvoker();

        invoker.register("read",  new ReadCommand(service));
        invoker.register("write",  new WriteCommand(service));
        invoker.register("erase",  new EraseCommand(service));
        invoker.register("erase_range",  new EraseRangeCommand(service));
        invoker.register("fullread",  new FullReadCommand(service));
        invoker.register("fullwrite",  new FullWriteCommand(service));
        invoker.register("1_FullWriteAndReadCompare",  new Script1(service));
        invoker.register("1_",  new Script1(service));
        invoker.register("2_PartialLBAWrite",  new Script2(service));
        invoker.register("2_",  new Script2(service));
        invoker.register("3_WriteReadAging",  new Script3(service));
        invoker.register("3_",  new Script3(service));
        invoker.register("4_EraseAndWriteAging",  new Script4(service));
        invoker.register("4_",  new Script4(service));
        invoker.register("exit",  new ExitCommand(service));

        while (true) {
            System.out.print("Shell> ");
            String input = scanner.nextLine().trim();

            if(input.startsWith("exit")) {
                System.out.print("Program terminated");
                break;
            }
            invoker.execute(input);
        }
    }
}

