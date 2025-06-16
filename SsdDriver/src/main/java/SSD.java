import command.CommandService;
import command.context.CommandContextFactory;
import command.validation.CommandValidator;
import command.validation.CommandValidatorFactory;
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

        if (!checkPreCondition(args)) {
            writeError();
            return;
        }

        try {
            CommandService.execute(CommandContextFactory.getCommandContext(args));
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

    private boolean checkPreCondition(String[] args) {
        if (args.length == 0) return false;
        CommandValidator validator = CommandValidatorFactory.getValidator(args[0]);
        if (validator == null) return false;

        return validator.validate(args);
    }

    private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSDConstants.OUTPUT_FILE_PATH))) {
            bw.write(SSDConstants.ERROR);
        } catch (IOException e) {
            // ignore
        }
    }
}
