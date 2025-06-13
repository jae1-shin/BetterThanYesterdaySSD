package command.script;

import command.common.CommandInvoker;
import command.common.ConsoleService;
import command.common.RunMode;
import logger.Logger;
import logger.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ScriptRunner implements RunMode {
    private final String filePath;

    public ScriptRunner(String filePath) {
        this.filePath = filePath;
    }

    Logger logger = Logger.getInstance();

    public void run() {
        if (!fileExists(filePath)) {
            logger.info("There's no File !");
            return;
        }

        ConsoleService service = new ConsoleService();
        CommandInvoker invoker = new CommandInvoker();

        invoker.register("1_FullWriteAndReadCompare", new FullWriteAndReadCompare(service));
        invoker.register("1_", new FullWriteAndReadCompare(service));
        invoker.register("2_PartialLBAWrite", new PartialLBAWrite(service));
        invoker.register("2_", new PartialLBAWrite(service));
        invoker.register("3_WriteReadAging", new WriteReadAging(service));
        invoker.register("3_", new WriteReadAging(service));
        invoker.register("4_EraseAndWriteAging", new EraseAndWriteAging(service));
        invoker.register("4_", new EraseAndWriteAging(service));

        List<String> scriptNames;
        try {
            scriptNames = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!isValidScriptNames(scriptNames, invoker)) {
            logger.info("Invalid Script Names!");
        }

        for (String script : scriptNames) {
            logger.result(script + "  ___  Run...", LoggerContext.CONSOLE_NO_NEWLINE);
            if (invoker.execute(script).shouldExit()) {
                return;
            }
        }
    }

    private boolean isValidScriptNames(List<String> scriptNames, CommandInvoker invoker) {
        if (scriptNames.isEmpty()) return false;
        for (String script : scriptNames) {
            if (invoker.hasCommand(script)) {
                return false;
            }
        }

        return true;
    }

    private boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}
