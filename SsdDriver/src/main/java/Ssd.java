import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ssd {
    public static void main(String[] args) {
        if (!isValidArgumentCount(args)) {
            writeError();
            return;
        }

        if (!isValidCommand(args[0])) {
            writeError();
            return;
        }

        if (!isValidAddressRange(args[1])) {
            writeError();
            return;
        }

        if (!isValidDataForWrite(args)) {
            writeError();
            return;
        }
    }

    private static boolean isValidDataForWrite(String[] args) {
        return "W".equals(args[0]) && args[2].matches("^0x[0-9A-Fa-f]{8}$");
    }

    private static boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < 0 || LBA >= 100) {
            return false;
        }
        return true;
    }

    private static boolean isValidCommand(String command) {
        return "W".equals(command) || "R".equals(command);
    }

    private static boolean isValidArgumentCount(String[] args) {
        return !(args.length < 2 || args.length > 3);
    }

    static private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_output.txt"))) {
            bw.write("ERROR");
        } catch (IOException e) {
            // ignore
        }
    }
}
