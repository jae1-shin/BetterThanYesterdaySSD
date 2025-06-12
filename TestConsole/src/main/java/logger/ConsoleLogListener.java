package logger;

public class ConsoleLogListener implements LogListener {
    private LogLevel threshold = LogLevel.INFO;

    public void setThreshold(LogLevel threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onLog(String timestamp, LogLevel level, String className, String message) {
        onLog(timestamp, level, className, message, true); // 기본 줄바꿈
    }

    public void onLog(String timestamp, LogLevel level, String className, String message, boolean useNewline) {
        if (level.ordinal() < threshold.ordinal()) return;

        if (useNewline) {
            System.out.println(message);
        } else {
            System.out.print(message);
        }
    }
}
