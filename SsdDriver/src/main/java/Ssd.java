import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ssd {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            writeError();
            return;
        }

        if (!("W".equals(args[0]) || "R".equals(args[0]))) {
            writeError();
            return;
        }

        int LBA = Integer.parseInt(args[1]);
        if (LBA < 0 || LBA >= 100) {
            writeError();
            return;
        }

        if (!args[2].matches("^0x[0-9A-Fa-f]{8}$")) {
            writeError();
            return;
        }
    }

    static private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_output.txt"))) {
            bw.write("ERROR");
        } catch (IOException e) {
            // ignore
        }
    }
}
