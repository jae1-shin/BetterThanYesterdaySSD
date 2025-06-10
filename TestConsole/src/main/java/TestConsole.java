import java.util.Scanner;

public class TestConsole {

    public String read(String commandStr){
        return "";
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
