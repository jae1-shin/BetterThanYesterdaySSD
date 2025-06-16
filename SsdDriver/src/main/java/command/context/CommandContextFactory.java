package command.context;

public class CommandContextFactory {

    public static CommandContext getCommandContext(String[] args) {
        return switch (args[0]) {
            case "W" -> new WriteCommandContext(Integer.parseInt(args[1]), args[2]);
            case "R" -> new ReadCommandContext(Integer.parseInt(args[1]));
            case "E" -> new EraseCommandContext(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            case "F" -> new FlushCommandContext();
            default -> new EmptyCommandContext();
        };
    }
}
