package logger;

// Logger.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final List<LogListener> listeners = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");

    private StackTraceElement getCaller() {
        return Thread.currentThread().getStackTrace()[3];
    }

    private String formatCaller(StackTraceElement caller) {
        String simpleClassName = caller.getClassName();
        int lastDot = simpleClassName.lastIndexOf(".");
        if (lastDot != -1) {
            simpleClassName = simpleClassName.substring(lastDot + 1);
        }
        return simpleClassName + "." + caller.getMethodName();
    }

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

    // DEBUG
    public void debug(String className, String message) {
        log(LogLevel.DEBUG, className, message, LoggerContext.DEFAULT);
    }

    public void debug(String message) {
        StackTraceElement caller = getCaller();
        log(LogLevel.DEBUG, formatCaller(caller), message, LoggerContext.DEFAULT);
    }

    public void debug(String message, LoggerContext context) {
        StackTraceElement caller = getCaller();
        log(LogLevel.DEBUG, formatCaller(caller), message, context);
    }

    // INFO
    public void info(String className, String message) {
        log(LogLevel.INFO, className, message, LoggerContext.DEFAULT);
    }

    public void info(String message) {
        StackTraceElement caller = getCaller();
        log(LogLevel.INFO, formatCaller(caller), message, LoggerContext.DEFAULT);
    }

    public void info(String message, LoggerContext context) {
        StackTraceElement caller = getCaller();
        log(LogLevel.INFO, formatCaller(caller), message, context);
    }

    // RESULT
    public void result(String className, String message) {
        log(LogLevel.RESULT, className, message, LoggerContext.DEFAULT);
    }

    public void result(String message) {
        StackTraceElement caller = getCaller();
        log(LogLevel.RESULT, formatCaller(caller), message, LoggerContext.DEFAULT);
    }

    public void result(String className, String message, boolean useNewline) {
        log(LogLevel.RESULT, className, message, new LoggerContext(true, useNewline));
    }

    public void result(String message, boolean useNewline) {
        StackTraceElement caller = getCaller();
        log(LogLevel.RESULT, formatCaller(caller), message, new LoggerContext(true, useNewline));
    }

    public void result(String className, String message, LoggerContext context) {
        log(LogLevel.RESULT, className, message, context);
    }

    public void result(String message, LoggerContext context) {
        StackTraceElement caller = getCaller();
        log(LogLevel.RESULT, formatCaller(caller), message, context);
    }

    // ERROR
    public void error(String className, String message) {
        log(LogLevel.ERROR, className, message, LoggerContext.DEFAULT);
    }

    public void error(String message) {
        StackTraceElement caller = getCaller();
        log(LogLevel.ERROR, formatCaller(caller), message, LoggerContext.DEFAULT);
    }

    public void error(String message, LoggerContext context) {
        StackTraceElement caller = getCaller();
        log(LogLevel.ERROR, formatCaller(caller), message, context);
    }
}
