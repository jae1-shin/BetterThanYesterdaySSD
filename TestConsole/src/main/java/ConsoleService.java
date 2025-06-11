import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConsoleService {
    public String read(int address){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ssd.jar", "R", Integer.toString(address) );
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

    public boolean write(int address ,String data){

        //SHELL write 3 0x00000
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "ssd.jar", "W", Integer.toString(address), data
            );


            pb.inheritIO(); // 콘솔 출력 연결

            Process process = null;

            process = pb.start();
            process.waitFor();

            String rtnStr = read(address);

            if(data.equals(rtnStr)) return true;

            process.destroy(); // 또는 process.destroyForcibly();
            System.out.println("Process was forcefully terminated.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return false;
    }

    public void fullRead(){
        ConsoleService consoleService = new ConsoleService();
        for(int i = 0; i < 100; i++) {
            String readValue = consoleService.read(i);
            System.out.println("Address " + i + ": " + readValue);
        }

    }

    public boolean fullWrite(String data){
        boolean eachResult = false;
        for(int i=0;i<100;i++){
            ConsoleService consoleService = new ConsoleService();
            eachResult = consoleService.write(i, data);
            if(eachResult == false) return false;
        }
        return true;
    }
}
