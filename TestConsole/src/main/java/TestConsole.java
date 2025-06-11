import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TestConsole {

    public String read(String commandStr){
        String[] args = commandStr.split(" ");
        if (args.length != 2) {
            return "ERROR no args";
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ssd.jar", "read", args[0], args[1]);
            pb.inheritIO();
            Process process = null;
            process = pb.start();
            process.waitFor();
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

    public void write(String commandStr){
        return;
    }

    public String fullRead(String commandStr){
        return "";
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
