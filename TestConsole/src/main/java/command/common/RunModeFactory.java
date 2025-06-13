package command.common;

import command.console.TestConsole;
import command.script.ScriptRunner;

public class RunModeFactory {
    public static RunMode create(String[] args) {
        if (args.length == 0) {
            return new TestConsole();
        } else if (args.length == 1) {
            return new ScriptRunner(args[0]);
        } else {
            throw new IllegalArgumentException("Invalid arguments. Usage: java Main [scriptFile]");
        }
    }
}
