package logger;

public class LoggerContext {
    public boolean enableConsole = true;
    public boolean useNewline = true;

    public LoggerContext(boolean enableConsole, boolean useNewline) {
        this.enableConsole = enableConsole;
        this.useNewline = useNewline;
    }

    public static final LoggerContext DEFAULT = new LoggerContext(true, true);
    public static final LoggerContext FILE_ONLY = new LoggerContext(false, true);
    public static final LoggerContext CONSOLE_NO_NEWLINE = new LoggerContext(true, false);
}