package command.buffer;

import command.context.CommandContext;
import command.context.EraseCommandContext;
import command.context.FlushCommandContext;
import command.impl.FlushCommand;
import command.CommandType;
import common.util.BufferUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class BufferOptimizer {
    private static BufferOptimizer controller = null;
    private List<CommandContext> buffer = Collections.emptyList();

    private BufferOptimizer() {
        // 외부 객체 생성 금지
    }

    public static BufferOptimizer getInstance() {
        if (controller == null) {
            controller = new BufferOptimizer();
        }
        return controller;
    }

    public void processCommand(CommandContext newCmd) throws IOException {
        getBufferFromDisk();
        flushBufferIfNeeded();

        if (newCmd.getType() == CommandType.WRITE) {
            applyIgnoreWrite(newCmd);
        } else {
            applyIgnoreErase(newCmd);
        }

        if (newCmd.getType() == CommandType.ERASE) {
            applyMergeErase(newCmd);
        }

        BufferUtil.rewriteBuffer(buffer);
    }

    private void flushBufferIfNeeded() throws IOException {
        if (buffer.size() != 5) return;
        FlushCommand flushCommand = new FlushCommand();
        flushCommand.execute(new FlushCommandContext());
        getBufferFromDisk();
    }

    private void getBufferFromDisk() {
        buffer = BufferUtil.getCommandList();
    }

    private void applyIgnoreWrite(CommandContext newCmd) {
        buffer.removeIf(cmd -> cmd.getType() == CommandType.WRITE && cmd.getLba() == newCmd.getLba());
        buffer.add(newCmd);
    }

    private void applyIgnoreErase(CommandContext newCmd) {
        int newStart = newCmd.getLba();
        int newEnd = newCmd.getLba() + newCmd.getSize() - 1;

        buffer.removeIf(cmd -> {
            if (cmd.getType() == CommandType.WRITE) {
                // Write 명령어의 LBA가 Erase 범위에 포함되면 삭제
                return cmd.getLba() >= newStart && cmd.getLba() <= newEnd;
            } else if (cmd.getType() == CommandType.ERASE) {
                // 기존 Erase 명령어 범위가 새 Erase 범위와 겹치면 삭제
                int start = cmd.getLba();
                int end = cmd.getLba() + cmd.getSize() - 1;
                return start >= newStart && end <= newEnd;
            }
            return false;
        });

        buffer.add(newCmd);
    }

    private void applyMergeErase(CommandContext newCmd) {
        if (buffer.size() < 2) return;

        ListIterator<CommandContext> iter = buffer.listIterator(buffer.size());
        CommandContext current = iter.previous();
        CommandContext previous = iter.previous();

        if (current.getType() == CommandType.ERASE && previous.getType() == CommandType.ERASE) {
            int start1 = previous.getLba();
            int end1 = previous.getLba() + previous.getSize() - 1;
            int start2 = current.getLba();
            int end2 = current.getLba() + current.getSize() - 1;

            // 두 Erase 명령어 범위가 인접하거나 겹치는지 확인
            if ((start2 <= end1 + 1) || (start1 <= end2 + 1)) {
                // 두 범위 합치기
                int mergedStart = Math.min(start1, start2);
                int mergedEnd = Math.max(end1, end2);
                int mergedSize = mergedEnd - mergedStart + 1;

                // Size는 10 이하인 경우 두 명령어 제거 후 합친 명령어 추가
                if (mergedSize <= 10) {
                    iter.remove();
                    iter.next();
                    iter.remove();
                    buffer.add(new EraseCommandContext(mergedStart, mergedSize));
                }
            }
        }
    }

    public List<CommandContext> getBuffer() {
        return buffer;
    }
}
