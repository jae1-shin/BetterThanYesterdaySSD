package command.impl;

import command.Command;
import command.context.CommandContext;
import command.CommandType;
import common.util.BufferUtil;

import java.io.IOException;
import java.util.*;

public class Flusher implements Command {

    @Override
    public void execute(CommandContext commandContext) throws IOException {
        flush();
    }

    public void flush() throws IOException {
        List<CommandContext> commandContextList = BufferUtil.getCommandList();
        for (CommandContext cmd : commandContextList) {
            if (cmd.getType() == CommandType.WRITE) {
                Writer writer = new Writer();
                writer.execute(cmd);
            } else if (cmd.getType() == CommandType.ERASE) {
                Eraser eraser = new Eraser();
                eraser.execute(cmd);
            }
        }

        BufferUtil.clearBuffer();
    }
}
