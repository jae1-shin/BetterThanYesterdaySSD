package command.buffer;

import command.Command;
import command.CommandContext;
import command.impl.Reader;
import command.CommandType;
import common.SSDConstants;
import common.util.BufferUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

public class BufferReader implements Command {

    private final Reader ssdReader;

    public BufferReader(Reader ssdReader) {
        this.ssdReader = ssdReader;
    }

    @Override
    public void execute(CommandContext commandContext) {
        read(commandContext.getLba());
    }

    public String read(int targetLBA) {
        List<CommandContext> commandContextList = BufferUtil.getCommandList();

        // 최신 명령 부터 확인
        commandContextList.sort(Comparator.comparingInt((CommandContext c) -> c.getOrder()).reversed());

        for (CommandContext cmd : commandContextList) {
            if (isTargetLBAWrited(targetLBA, cmd)) {
                writeOutput(cmd.getData());
                return cmd.getData();
            }
            if (isTargetLBAErased(targetLBA, cmd)) {
                writeOutput(SSDConstants.DEFAULT_DATA);
                return SSDConstants.DEFAULT_DATA;
            }
        }

        // 못찾은 경우
        try {
            return ssdReader.read(targetLBA);
        } catch (IOException e) {
            // ignore
            return "";
        }
    }

    private void writeOutput(String readStr) {
        try {
            Files.writeString(Paths.get(SSDConstants.OUTPUT_FILE_PATH),
                    readStr,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            // ignore
        }
    }

    private boolean isTargetLBAErased(int targetLBA, CommandContext cmd) {
        return cmd.getType() == CommandType.ERASE &&
                targetLBA >= cmd.getLba() && targetLBA < (cmd.getLba() + cmd.getSize());
    }

    private boolean isTargetLBAWrited(int targetLBA, CommandContext cmd) {
        return cmd.getType() == CommandType.WRITE && cmd.getLba() == targetLBA;
    }
}
