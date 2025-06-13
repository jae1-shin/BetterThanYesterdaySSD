import command.CommandContext;
import command.CommandFactory;
import command.validator.ArgsValidator;
import common.SSDConstants;
import common.util.BufferUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SSD {
    public static final int LBA_MAX_COUNT = 100;

    public void processCommand(String[] args) {
        try  {
            initFiles();
        } catch (IOException e) {
            // ignore
        }

        if (!ArgsValidator.checkPreCondition(args)) {
            writeError();
            return;
        }

        try {
            CommandContext commandContext = BufferUtil.getCommandFromSsdArgs(args);
            CommandFactory.execute(commandContext);
        } catch (Exception e) {
            // ignore
        }
    }

    void initFiles() throws IOException {
        checkAndClearOutputFile();
        checkAndCreateNandFile();
        checkAndCreateBuffer();
    }

    private void checkAndClearOutputFile() throws IOException {
        Files.writeString(Paths.get(SSDConstants.OUTPUT_FILE_PATH), "");
    }

    private void checkAndCreateNandFile() throws IOException {
        File file = new File(SSDConstants.SSD_NAND_FILE);
        if (file.exists()) return;
        writeDefaultData();
    }

    private void writeDefaultData() throws IOException {
        Files.writeString(Paths.get(SSDConstants.SSD_NAND_FILE), (SSDConstants.DEFAULT_DATA).repeat(LBA_MAX_COUNT));
    }

    private void checkAndCreateBuffer() throws IOException {
        File bufferDir = BufferUtil.checkAndCreateBufferDir();
        BufferUtil.checkAndCreateEmptyBufferFiles(bufferDir);
    }

    private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSDConstants.OUTPUT_FILE_PATH))) {
            bw.write(SSDConstants.ERROR);
        } catch (IOException e) {
            // ignore
        }
    }
}
