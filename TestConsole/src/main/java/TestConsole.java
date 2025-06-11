
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

    public String read(int address){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ssd.jar", "R", Integer.toString(address) );
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

    public boolean write(String commandStr){

        //SHELL write 3 0x00000

            String[] parts = commandStr.split("\\s+");
           String commandName = parts[0];
           String[] commandArgs = new String[parts.length - 1];
           System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);
           String address= commandArgs[0];
            String data= commandArgs[1];


        ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", "\"C:\\Users\\User\\IdeaProjects\\BetterThanYesterdaySSD\\TestConsole\\src\\main\\resources\\SSD.jar\"", "W", address, data
        );

        pb.inheritIO(); // 콘솔 출력 연결

        Process process = null;

        try {
            process = pb.start();
            process.waitFor();
            List<String> blocks  = loadBlocks("ssd_nand.txt");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return true;
    }

    public void fullRead(){

    }

    public boolean fullWrite(String commandStr){
        return true;
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
            } else if (line.startsWith("FullWriteAndReadCompare")) {
                System.out.println("FullWriteAndReadCompare");
            } else if (line.startsWith("PartialLBAWrite")) {
                System.out.println("PartialLBAWrite");
            } else if (line.startsWith("WriteReadAging")) {
                System.out.println("WriteReadAging");
            } else if (line.startsWith("exit")) {
                System.out.println("Program terminated");
                break;
            } else {
                System.out.println("wrong input");
            }
        }
    }
}

