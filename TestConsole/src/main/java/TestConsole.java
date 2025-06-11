
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

public class TestConsole {

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

    public String write(int address ,String data){

        //SHELL write 3 0x00000
        /*ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", "\"C:\\Users\\User\\IdeaProjects\\BetterThanYesterdaySSD\\TestConsole\\src\\main\\resources\\SSD.jar\"", "W", Integer.toString(address), data
        );*/

        ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", "ssd.jar", "W", Integer.toString(address), data
        );

        pb.inheritIO(); // 콘솔 출력 연결

        Process process = null;

        try {
            process = pb.start();
            process.waitFor();

            String rtnStr = read(address);

            if(data.equals(rtnStr)) return "Done";

            process.destroy(); // 또는 process.destroyForcibly();
            System.out.println("Process was forcefully terminated.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "ERROR";
    }


    public void fullRead(){

    }

    public boolean fullWrite(String commandStr){
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Shell> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

//            String[] parts = line.split("\\s+");
//            String commandName = parts[0];
//            String[] commandArgs = new String[parts.length - 1];
//            System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

            if (line.equals("exit")) {
                System.out.println("종료되었습니다");
                return;
            } else {
                System.out.println("Command 실행");;
            }
        }
    }
}
