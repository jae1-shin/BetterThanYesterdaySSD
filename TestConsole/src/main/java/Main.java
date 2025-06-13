import command.common.RunMode;
import command.common.RunModeFactory;
import command.console.TestConsole;
import command.script.ScriptRunner;

public class Main {

    public static void main(String[] args) {
        RunMode mode = RunModeFactory.create(args);
        mode.run();
    }
}
