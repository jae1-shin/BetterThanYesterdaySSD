package command.impl.buffer;

import command.context.CommandContext;
import command.impl.ReadCommand;
import common.SSDConstants;
import common.util.BufferUtil;

import java.io.IOException;
import java.util.List;

public class ReadBufferCommand extends ReadCommand {

    @Override
    protected void read(int targetLBA) throws IOException {
        List<CommandContext> commandContextList = BufferUtil.getCommandContextList();

        for (int i = commandContextList.size() - 1; i >= 0; i--) {
            CommandContext cmd = commandContextList.get(i);
            if (isTargetLBAWrited(targetLBA, cmd)) {
                writeOutput(cmd.getData());
                return;
            }
            if (isTargetLBAErased(targetLBA, cmd)) {
                writeOutput(SSDConstants.DEFAULT_DATA);
                return;
            }
        }

        // 못찾은 경우
        super.read(targetLBA);
    }

    private boolean isTargetLBAErased(int targetLBA, CommandContext cmd) {
        return cmd.isErase() &&
                targetLBA >= cmd.getLba() && targetLBA < (cmd.getLba() + cmd.getSize());
    }

    private boolean isTargetLBAWrited(int targetLBA, CommandContext cmd) {
        return cmd.isWirte() && cmd.getLba() == targetLBA;
    }
}
