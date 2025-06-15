package command.script;

import command.common.Command;
import command.common.ConsoleService;

public abstract class TestScript extends Command {
    public TestScript(ConsoleService service) {
        super(service);
        validator = new TestScriptValidator();
    }
}
