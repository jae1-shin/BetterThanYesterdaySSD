
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

    public String read(int address) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ssd.jar", "R", Integer.toString(address));
            pb.inheritIO();
            Process process = null;
            process = pb.start();
            process.waitFor();
            System.out.println("process가 실행됐어요");
        } catch (IOException | InterruptedException e) {
            return "ERROR process " + e.getMessage();
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("ssd_output.txt"));
        } catch (IOException e) {
            return "ERROR file " + e.getMessage();
        }
        return lines.get(0);
    }

    public static List<String> loadBlocks(String content) throws Exception {
        //String content = Files.readString(Paths.get(filePath)).replaceAll("\\s+", ""); // 줄바꿈 제거

        List<String> blocks = new ArrayList<>();
        for (int i = 0; i < content.length(); i += 10) {
            if (i + 10 <= content.length()) {
                blocks.add(content.substring(i, i + 10));
            }
        }

        return blocks;
    }

    public boolean write(int address, String data) {


        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "ssd.jar", "W", Integer.toString(address), data
            );

            pb.inheritIO(); // 콘솔 출력 연결
            Process process = null;
            process = pb.start();
            process.waitFor();

            String rtnStr = read(address);

            process.destroy(); // 또는 process.destroyForcibly();
            return data.equals(rtnStr);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void fullRead() {


    }

    public boolean fullWrite(String data) {
        boolean eachResult = false;
        for (int i = 0; i < 100; i++) {
            eachResult = write(i, data);
            if (eachResult == false) return false;
        }
        return true;
    }

    public boolean readCompare(int LBA, String value) {
        return value.equals(this.read(LBA));
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

