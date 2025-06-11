import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SsdReader {

    private static final String READ_FILE_PATH = "ssd_nand.txt";
    private static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    private static final int BLOCK_SIZE = 10;

    public void read(int LBA) throws IOException {
        if (isLBAOutOfRange(LBA)) {
            writeError();
            return;
        }

        long offset = (long) LBA * BLOCK_SIZE;

        try (RandomAccessFile raf = new RandomAccessFile(READ_FILE_PATH, "r")) {
            raf.seek(offset);

            byte[] buffer = new byte[BLOCK_SIZE];
            int bytesRead = raf.read(buffer);

            if (bytesRead == -1) {
                writeError();
                return;
            }

            String readStr = new String(buffer, 0, bytesRead);

            Files.writeString(Paths.get(OUTPUT_FILE_PATH),
                    readStr,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isLBAOutOfRange(int LBA) {
        return LBA < 0 || LBA > 99;
    }

    private static void writeError() throws IOException {
        Files.writeString(Paths.get(OUTPUT_FILE_PATH),
                "ERROR",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
