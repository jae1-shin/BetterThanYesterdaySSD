import java.io.IOException;

public interface Command {
    void execute(String commandStr) throws IOException, InterruptedException;
}
