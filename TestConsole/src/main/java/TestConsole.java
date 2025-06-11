
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

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
        invoker.register("fullread",  new FullReadCommand(service));
        invoker.register("fullwrite",  new FullWriteCommand(service));
        invoker.register("1_FullWriteAndReadCompare",  new Script1(service));
        invoker.register("1_",  new Script1(service));
        invoker.register("2_PartialLBAWrite",  new Script2(service));
        invoker.register("2_",  new Script2(service));
        invoker.register("3_WriteReadAging",  new Script3(service));
        invoker.register("3_",  new Script3(service));
        invoker.register("exit",  new ExitCommand(service));

        while (true) {
            System.out.print("Shell> ");
            String input = scanner.nextLine().trim();
            invoker.execute(input);
        }
    }
}

