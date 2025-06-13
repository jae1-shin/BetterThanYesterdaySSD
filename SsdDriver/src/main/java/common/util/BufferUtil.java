package common.util;

import command.context.CommandContext;
import command.context.CommandContextFactory;
import common.SSDConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class BufferUtil {

    public static List<CommandContext> getCommandList() {
        File bufferDir = new File(SSDConstants.BUFFER_PATH);
        File[] files = bufferDir.listFiles();
        if (files == null) return Collections.emptyList();

        List<File> sortedFiles = Arrays.stream(files)
                .filter(f -> !f.getName().contains("empty"))
                .sorted(Comparator.comparing(File::getName))
                .collect(Collectors.toList());

        List<CommandContext> commandContextList = new ArrayList<>();
        for (File file : sortedFiles) {
            // TODO CommandValidator로 선 검증
            String[] args = file.getName().substring(2).split("_");
//            CommandValidator validator = CommandValidatorFactory.getValidator(args[0]);
//            if (validator == null) continue;
            commandContextList.add(CommandContextFactory.getCommandContext(args));
        }
        return commandContextList;
    }

    public static void rewriteBuffer(List<CommandContext> commandContexts) {
        deleteBufferDirAndFiles();
        try {
            File bufferDir = checkAndCreateBufferDir();
            createBufferFiles(commandContexts, bufferDir);
            checkAndCreateEmptyBufferFiles(bufferDir);
        } catch (IOException e) {
            // ignore
        }
    }

    private static void createBufferFiles(List<CommandContext> commandContexts, File bufferDir) throws IOException {
        int order = 1;
        for (CommandContext commandContext : commandContexts) {
            String fileName = String.format("%d_%s", order++, commandContext.getCommandFullName());
            Files.writeString(Paths.get(bufferDir.getPath(), fileName), "");
        }
    }

    public static void clearBuffer() {
        try {
            deleteBufferDirAndFiles();

            File bufferDir = checkAndCreateBufferDir();
            checkAndCreateEmptyBufferFiles(bufferDir);
        } catch (Exception e) {
            // ignore
        }
    }

    public static void deleteBufferDirAndFiles() {
        File bufferDir = new File(SSDConstants.BUFFER_PATH);

        if (bufferDir.exists()) {
            for (File file : bufferDir.listFiles()) {
                file.delete();
            }
            bufferDir.delete();
        }
    }

    public static File checkAndCreateBufferDir() {
        File bufferDir = new File(SSDConstants.BUFFER_PATH);
        if (!bufferDir.exists()) {
            bufferDir.mkdirs();
        }
        return bufferDir;
    }

    public static void checkAndCreateEmptyBufferFiles(File bufferDir) throws IOException {
        for (int bufferNum = 1; bufferNum <= SSDConstants.BUFFER_SIZE; bufferNum++) {
            if (existBufferFile(bufferDir, bufferNum)) continue;
            Files.writeString(Paths.get(bufferDir.getPath(), getBufferDefaultFileName(bufferNum)), "");
        }
    }

    private static boolean existBufferFile(File bufferDir, int bufferNum) {
        final String bufferPrefix = getBufferFilePrefix(bufferNum);
        File[] bufferFiles = bufferDir.listFiles((dir, name) -> name.startsWith(bufferPrefix));

        return bufferFiles != null && bufferFiles.length > 0;
    }

    private static String getBufferFilePrefix(int bufferNum) {
        return bufferNum + "_";
    }

    private static String getBufferDefaultFileName(int bufferNum) {
        return getBufferFilePrefix(bufferNum) + "empty";
    }
}
