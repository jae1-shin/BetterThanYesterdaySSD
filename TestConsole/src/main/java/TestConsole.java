
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


        while (true) {
            System.out.print("Shell> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String commandName = parts[0];
            String[] commandArgs = new String[parts.length - 1];
            System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

            if (line.startsWith("read")) {
                //String readValue = read(Integer.parseInt(commandArgs[0]));

                System.out.println("read");

            } else if (line.startsWith("write")) {
                System.out.println("write");
            } else if (line.startsWith("fullread")) {
                System.out.println("fullread");
            } else if (line.startsWith("fullwrite")) {
                System.out.println("fullwrite");
            } else if (line.startsWith("1_FullWriteAndReadCompare")) {
                System.out.println("FullWriteAndReadCompare");
            } else if (line.startsWith("2_PartialLBAWrite")) {
                System.out.println("PartialLBAWrite");
            } else if (line.startsWith("3_WriteReadAging")) {
                System.out.println("WriteReadAging");
            } else if (line.startsWith("exit")) {
                System.out.println("Program terminated");
                break;
            } else {
                System.out.println("Command 실행");
            }
        }
    }
}

