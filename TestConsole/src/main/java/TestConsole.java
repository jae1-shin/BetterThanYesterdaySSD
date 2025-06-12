
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

