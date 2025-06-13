package command.impl;

import command.Command;
import command.context.CommandContext;
import common.SSDConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Reader implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        read(commandContext.getLba());
    }

    public String read(int LBA) throws IOException {
        long offset = (long) LBA * SSDConstants.BLOCK_SIZE;

        try (RandomAccessFile raf = new RandomAccessFile(SSDConstants.SSD_NAND_FILE, "r")) {
            raf.seek(offset);

            byte[] buffer = new byte[SSDConstants.BLOCK_SIZE];
            int bytesRead = raf.read(buffer);

            String readStr = new String(buffer, 0, bytesRead);

            Files.writeString(Paths.get(SSDConstants.OUTPUT_FILE_PATH),
                    readStr,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return readStr;
        }
    }
}
