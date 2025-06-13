package command.impl;

import command.Command;
import command.CommandContext;
import common.SSDConstants;

import java.io.*;

public class Writer implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        write(commandContext.getLba(), commandContext.getData());
    }

    public void write(int address, String data) throws IOException {
        File file = new File(SSDConstants.SSD_NAND_FILE);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(address * SSDConstants.BLOCK_SIZE);
        raf.writeBytes(data);
        raf.close();
    }
}
