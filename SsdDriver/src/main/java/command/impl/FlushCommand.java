package command.impl;

import command.Command;
import command.context.CommandContext;
import command.CommandType;
import common.util.BufferUtil;

import java.io.IOException;
import java.util.*;

public class FlushCommand implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        flush();
    }

    public void flush() throws IOException {
        List<CommandContext> commandContextList = BufferUtil.getCommandList();
        for (CommandContext cmd : commandContextList) {
            if (cmd.getType() == CommandType.WRITE) {
                WriteCommand writeCommand = new WriteCommand();
                writeCommand.execute(cmd);
            } else if (cmd.getType() == CommandType.ERASE) {
                EraseCommand eraseCommand = new EraseCommand();
                eraseCommand.execute(cmd);
            }
        }

        BufferUtil.clearBuffer();
    }
}
