package command.impl;

import command.Command;
import command.context.CommandContext;
import common.util.BufferUtil;

import java.io.IOException;
import java.util.*;

public class FlushCommand implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        flush();
    }

    private void flush() throws IOException {
        WriteCommand writeCommand = new WriteCommand();
        EraseCommand eraseCommand = new EraseCommand();
        List<CommandContext> commandContextList = BufferUtil.getCommandContextList();
        for (CommandContext cmd : commandContextList) {
            if (cmd.isWrite()) {
                writeCommand.execute(cmd);
            } else if (cmd.isErase()) {
                eraseCommand.execute(cmd);
            }
        }

        BufferUtil.clearBuffer();
    }
}
