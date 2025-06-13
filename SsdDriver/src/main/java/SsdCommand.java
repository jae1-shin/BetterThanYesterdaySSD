import java.io.IOException;

interface SsdCommand {
    public void execute(Command command) throws IOException;
}
