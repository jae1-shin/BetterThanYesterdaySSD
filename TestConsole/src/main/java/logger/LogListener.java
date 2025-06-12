package logger;

public interface LogListener {
    void onLog(String timestamp, LogLevel level, String className, String message);
}