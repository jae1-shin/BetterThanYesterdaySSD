package command.script;

import command.common.CommandValidator;

public class TestScriptValidator extends CommandValidator {
    @Override
    public String validCheck(String[] args) {
        return NO_NEED_TO_VALID_CHECK;
    }
}
