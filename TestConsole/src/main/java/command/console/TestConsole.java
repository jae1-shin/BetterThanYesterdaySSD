package command.console;

import command.common.CommandInvoker;
import command.common.ConsoleService;
import command.common.RunMode;
import command.script.FullWriteAndReadCompare;
import logger.Logger;
import command.script.PartialLBAWrite;
import command.script.WriteReadAging;
import command.script.EraseAndWriteAging;

import java.io.InputStream;
import java.util.Scanner;

public class TestConsole implements RunMode {

    public static final String PROGRAM_TERMINATED = "command.console.TestConsole terminated";
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
        invoker.register("flush",  new FlushCommand(service));
        invoker.register("fullread",  new FullReadCommand(service));
        invoker.register("fullwrite",  new FullWriteCommand(service));
        invoker.register("1_FullWriteAndReadCompare",  new FullWriteAndReadCompare(service));
        invoker.register("1_",  new FullWriteAndReadCompare(service));
        invoker.register("2_PartialLBAWrite",  new PartialLBAWrite(service));
        invoker.register("2_",  new PartialLBAWrite(service));
        invoker.register("3_WriteReadAging",  new WriteReadAging(service));
        invoker.register("3_",  new WriteReadAging(service));
        invoker.register("4_EraseAndWriteAging",  new EraseAndWriteAging(service));
        invoker.register("4_",  new EraseAndWriteAging(service));
        invoker.register("exit",  new ExitCommand(service));
        invoker.register("help",  new HelpCommand(service));

        while (true) {
            System.out.print("Shell> ");
            String input = scanner.nextLine().trim();

            if(invoker.execute(input).shouldExit()) {
                Logger.getInstance().result(PROGRAM_TERMINATED);
                break;
            }
        }
    }
}

