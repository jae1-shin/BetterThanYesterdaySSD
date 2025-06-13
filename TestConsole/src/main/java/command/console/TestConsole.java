package command.console;

import command.common.*;
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
        CommandFactory factory = new CommandFactory();
        CommandInvoker invoker = new CommandInvoker();

        CommandRegistrar.registerConsoleCommands(invoker, factory, service);

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

