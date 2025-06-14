import command.CommandService;
import command.context.CommandContextFactory;
import common.SSDConstants;
import common.util.BufferUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SSD {
    public static final String DATA_FORMAT = "^0x[0-9A-Fa-f]{8}$";
    public static final String WRITE_COMMAND = "W";
    public static final String READ_COMMAND = "R";
    public static final String ERASE_COMMAND = "E";
    public static final String FLUSH_COMMAND = "F";
    public static final int ARGUMENT_COMMAND_INDEX = 0;
    public static final int ARGUMENT_ADDRESS_INDEX = 1;
    public static final int ARGUMENT_DATA_INDEX = 2;
    public static final int LBA_MIN = 0;
    public static final int LBA_MAX = 99;
    public static final int LBA_MAX_COUNT = 100;
    public static final int ARGUMENT_MAX_COUNT = 3;

    public void processCommand(String[] args) {
        try {
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

    private boolean isWriteCommand(String[] args) {
        return WRITE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private boolean isReadCommand(String[] args) {
        return READ_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private boolean isEraseCommand(String[] args) {
        return ERASE_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private boolean isFlushCommand(String[] args) {
        return FLUSH_COMMAND.equals(args[ARGUMENT_COMMAND_INDEX]);
    }

    private boolean checkPreCondition(String[] args) {
        if (!isValidArgumentCount(args)) return false;
        if (!isValidCommand(args[ARGUMENT_COMMAND_INDEX])) return false;
        if (!isFlushCommand(args) && !isValidAddressRange(args[ARGUMENT_ADDRESS_INDEX])) return false;
        if (!isValidDataForWrite(args)) return false;
        if (!isValidDataForErase(args)) return false;

        return true;
    }

    private boolean isValidDataForWrite(String[] args) {
        if (isReadCommand(args) || isEraseCommand(args) || isFlushCommand(args)) return true;
        return isWriteCommand(args) && args[ARGUMENT_DATA_INDEX].matches(DATA_FORMAT);
    }

    private boolean isValidDataForErase(String[] args) {
        if (isReadCommand(args) || isWriteCommand(args) || isFlushCommand(args)) return true;
        if (Integer.parseInt(args[ARGUMENT_DATA_INDEX]) < 0 || Integer.parseInt(args[ARGUMENT_DATA_INDEX]) > 10)
            return false;
        int lastLBA = Integer.parseInt(args[ARGUMENT_ADDRESS_INDEX]) + Integer.parseInt(args[ARGUMENT_DATA_INDEX]) - 1;
        return isEraseCommand(args) && isValidAddressRange(Integer.toString(lastLBA));
    }

    private boolean isValidAddressRange(String address) {
        int LBA = Integer.parseInt(address);
        if (LBA < LBA_MIN || LBA > LBA_MAX) {
            return false;
        }
        return true;
    }

    private boolean isValidCommand(String command) {
        return WRITE_COMMAND.equals(command) || READ_COMMAND.equals(command) || ERASE_COMMAND.equals(command) || FLUSH_COMMAND.equals(command);
    }

    private boolean isValidArgumentCount(String[] args) {
        if (isFlushCommand(args)) {
            return args.length == 1; // Flush command should have only one argument
        }
        return !(args.length < ARGUMENT_DATA_INDEX || args.length > ARGUMENT_MAX_COUNT);
    }

    private void writeError() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSDConstants.OUTPUT_FILE_PATH))) {
            bw.write(SSDConstants.ERROR);
        } catch (IOException e) {
            // ignore
        }
    }
}
