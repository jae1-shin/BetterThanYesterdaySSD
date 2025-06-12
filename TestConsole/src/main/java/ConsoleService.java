import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConsoleService {

    public static final int TOTAL_LBA_COUNT = 100;

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

    public boolean write(int address, String data){
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "ssd.jar", "W", Integer.toString(address), data
            );

            pb.inheritIO(); // 콘솔 출력 연결

            Process process = pb.start();
            process.waitFor();

            if(data.equals(read(address))) return true;

            process.destroy(); // 또는 process.destroyForcibly();
            System.out.println("Process was forcefully terminated.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return false;
    }

    public void fullRead(){
        for(int i = 0; i < TOTAL_LBA_COUNT; i++){
            System.out.println("LBA " + i + ": " + read(i));
        }
    }

    public boolean fullWrite(String data){
        boolean eachResult = false;
        for(int i = 0; i< TOTAL_LBA_COUNT; i++){
            eachResult = write(i,data);
            if(eachResult == false) return false;
        }
        return true;
    }

    public boolean readCompare(int LBA, String value) {
        return value.equals(this.read(LBA));
    }

    public void help() {
        System.out.println("Team: BetterThanYesterday");
        System.out.println("Members: 신재원, 정혜원, 문영민, 조효민, 류지우, 서인규");
        System.out.println();
        System.out.println();
        System.out.println("Available commands:");
        System.out.println("write {lba} {data} - Write data to the specified LBA");
        System.out.println("read {lba} - Read data from the specified LBA");
        System.out.println("exit - Exit the console");
        System.out.println("help - Display this help message");
        System.out.println("fullwrite {data} - Write the same data to all LBAs");
        System.out.println("fullread - Read all LBAs and display their values");
        System.out.println();
        System.out.println("Note: ");
        System.out.println("{lba} must be an integer between 0 and 99");
        System.out.println("{data} must be 4-byte unsigned hexadecimal (0x00000000 - 0xFFFFFFFF)");
    }
}
