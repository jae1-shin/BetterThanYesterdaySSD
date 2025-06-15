package command.common;

import logger.Logger;

public class CommandResult {
    private final boolean shouldExit;
    private final String message;

    // 정적 팩토리 메서드로 표현력 향상
    public static final CommandResult PASS = new CommandResult();
    public static final CommandResult EXIT = new CommandResult(true);


    public CommandResult() {
        this.shouldExit = false;
        this.message = "";
    }

    public CommandResult(boolean shouldExit) {
        this.shouldExit = shouldExit;
        this.message = "";
    }

    public CommandResult(boolean shouldExit, String message) {
        this.shouldExit = shouldExit;
        this.message = message;
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    public String getMessage() {
        return message;
    }

    public static CommandResult pass(String message) {
        Logger.getInstance().result(message);
        return PASS;
    }

    public static CommandResult error(String message) {
        Logger.getInstance().error(message);
        return new CommandResult(false, message);
    }

    public static CommandResult scriptFail(String message) {
        Logger.getInstance().error(message);
        return new CommandResult(true, message);
    }
}
