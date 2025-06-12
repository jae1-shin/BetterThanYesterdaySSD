package logger;

public class LoggerHolder {
    public static final Logger logger;
    public static final ConsoleLogListener consoleListener;

    static {
        logger = new Logger();
        consoleListener = new ConsoleLogListener();
        consoleListener.setThreshold(LogLevel.INFO); // 콘솔에 INFO 이상만 출력

        logger.addListener(consoleListener);
        logger.addListener(new RollingFileLogListener());
    }
}