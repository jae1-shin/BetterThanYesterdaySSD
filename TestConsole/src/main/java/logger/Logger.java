package logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final List<LogListener> listeners = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");

    public void addListener(LogListener listener) {
        listeners.add(listener);
    }

    public void log(LogLevel level, String className, String message, boolean useNewline) {
        log(level, className, message, new LoggerContext(true, useNewline));
    }

    public void log(LogLevel level, String className, String message, LoggerContext context) {
        String timestamp = LocalDateTime.now().format(formatter);
        for (LogListener listener : listeners) {
            if (listener instanceof ConsoleLogListener) {
                if (context.enableConsole) {
                    ((ConsoleLogListener) listener).onLog(timestamp, level, className, message, context.useNewline);
                }
            } else {
                listener.onLog(timestamp, level, className, message);
            }
        }
    }

    // Overloaded convenience methods
    public void debug(String className, String message) {
        log(LogLevel.DEBUG, className, message, LoggerContext.DEFAULT);
    }

    public void info(String className, String message) {
        log(LogLevel.INFO, className, message, LoggerContext.DEFAULT);
    }

    public void result(String className, String message) {
        log(LogLevel.RESULT, className, message, LoggerContext.DEFAULT);
    }

    public void result(String className, String message, boolean useNewline) {
        log(LogLevel.RESULT, className, message, new LoggerContext(true, useNewline));
    }

    public void result(String className, String message, LoggerContext context) {
        log(LogLevel.RESULT, className, message, context);
    }

    public void error(String className, String message) {
        log(LogLevel.ERROR, className, message, LoggerContext.DEFAULT);
    }
}