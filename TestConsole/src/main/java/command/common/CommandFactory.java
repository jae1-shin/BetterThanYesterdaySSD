package command.common;

import command.console.*;
import command.script.EraseAndWriteAging;
import command.script.FullWriteAndReadCompare;
import command.script.PartialLBAWrite;
import command.script.WriteReadAging;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, CommandCreator> registry = new HashMap<String, CommandCreator>();

    public CommandFactory() {
        register("read", new CommandCreator() {public Command create(ConsoleService service) { return new ReadCommand(service);}});
        register("write", new CommandCreator() {public Command create(ConsoleService service) { return new WriteCommand(service);}});
        register("erase", new CommandCreator() {public Command create(ConsoleService service) { return new EraseCommand(service);}});
        register("erase_range", new CommandCreator() {public Command create(ConsoleService service) { return new EraseRangeCommand(service);}});
        register("flush", new CommandCreator() {public Command create(ConsoleService service) { return new FlushCommand(service);}});
        register("fullread", new CommandCreator() {public Command create(ConsoleService service) { return new FullReadCommand(service);}});
        register("fullwrite", new CommandCreator() {public Command create(ConsoleService service) { return new FullWriteCommand(service);}});
        register("help", new CommandCreator() {public Command create(ConsoleService service) { return new HelpCommand(service);}});
        register("exit", new CommandCreator() {public Command create(ConsoleService service) { return new ExitCommand(service);}});

        register("1_FullWriteAndReadCompare", new CommandCreator() {public Command create(ConsoleService service) { return new FullWriteAndReadCompare(service);}});
        register("1_", new CommandCreator() {public Command create(ConsoleService service) { return new FullWriteAndReadCompare(service);}});
        register("2_PartialLBAWrite", new CommandCreator() {public Command create(ConsoleService service) { return new PartialLBAWrite(service);}});
        register("2_", new CommandCreator() {public Command create(ConsoleService service) { return new PartialLBAWrite(service);}});
        register("3_WriteReadAging", new CommandCreator() {public Command create(ConsoleService service) { return new WriteReadAging(service);}});
        register("3_", new CommandCreator() {public Command create(ConsoleService service) { return new WriteReadAging(service);}});
        register("4_EraseAndWriteAging", new CommandCreator() {public Command create(ConsoleService service) { return new EraseAndWriteAging(service);}});
        register("4_", new CommandCreator() {public Command create(ConsoleService service) { return new EraseAndWriteAging(service);}});
    }

    public void register(String name, CommandCreator creator) {
        registry.put(name.toLowerCase(), creator);
    }

    public Command create(String name, ConsoleService service) {
        CommandCreator creator = registry.get(name.toLowerCase());
        if (creator == null)
            throw new RuntimeException("구현되지 않은 Command 입니다" + name);
        return creator.create(service);
    }
}
