package command.common;

public class CommandRegistrar {
    public static void registerConsoleCommands(CommandInvoker invoker, CommandFactory factory, ConsoleService service) {
        String[] commands = {
                "read", "write", "erase", "flush", "help", "exit", "fullread", "fullwrite", "erase_range",
                "1_", "1_FullWriteAndReadCompare",
                "2_", "2_PartialLBAWrite",
                "3_", "3_WriteReadAging",
                "4_", "4_EraseAndWriteAging"
        };

        for (String cmd : commands) {
            invoker.register(cmd, factory.create(cmd, service));
        }
    }

    public static void registerScriptCommands(CommandInvoker invoker, CommandFactory factory, ConsoleService service) {
        String[] commands = {
                "1_", "1_FullWriteAndReadCompare",
                "2_", "2_PartialLBAWrite",
                "3_", "3_WriteReadAging",
                "4_", "4_EraseAndWriteAging"
        };

        for (String cmd : commands) {
            invoker.register(cmd, factory.create(cmd, service));
        }
    }
}
