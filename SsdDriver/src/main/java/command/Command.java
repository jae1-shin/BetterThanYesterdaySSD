package command;

import java.io.IOException;

public interface Command {
    public void execute(CommandContext commandContext) throws IOException;
}
