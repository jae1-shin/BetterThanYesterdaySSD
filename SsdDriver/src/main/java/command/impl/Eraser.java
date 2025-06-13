package command.impl;

import command.Command;
import command.context.CommandContext;
import common.SSDConstants;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Eraser implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        erase(commandContext.getLba(), commandContext.getSize());
    }

    public void erase(int address, int size) throws IOException {
        File file = new File(SSDConstants.SSD_NAND_FILE);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(address * SSDConstants.BLOCK_SIZE);
        raf.writeBytes(SSDConstants.DEFAULT_DATA.repeat(size));
        raf.close();
    }
}
