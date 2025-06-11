import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SsdReader {

    public void read(int LBA) throws IOException {
        long offset = (long) LBA * SsdConstants.BLOCK_SIZE;

        try (RandomAccessFile raf = new RandomAccessFile(SsdConstants.SSD_NAND_FILE, "r")) {
            raf.seek(offset);

            byte[] buffer = new byte[SsdConstants.BLOCK_SIZE];
            int bytesRead = raf.read(buffer);

            if (bytesRead == -1) {
                writeError();
                return;
            }

            String readStr = new String(buffer, 0, bytesRead);

            Files.writeString(Paths.get(SsdConstants.OUTPUT_FILE_PATH),
                    readStr,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeError() throws IOException {
        Files.writeString(Paths.get(SsdConstants.OUTPUT_FILE_PATH),
                "ERROR",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
