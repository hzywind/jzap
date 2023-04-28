package jzap.zapcore;

import java.time.Instant;

public class Entry {

    private Level level;
    private Instant time;
    private String loggerName;
    private String message;
    private EntryCaller caller;
    private String stack;

    public Entry(Level level, Instant time, String loggerName, String message) {
        this.level = level;
        this.time = time;
        this.loggerName = loggerName;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public Instant getTime() {
        return time;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public String getMessage() {
        return message;
    }

    public EntryCaller getCaller() {
        return caller;
    }

    public void setCaller(EntryCaller caller) {
        this.caller = caller;
    }

    public void setStack(String value) {
        this.stack = value;
    }

    public String getStack() {
        return stack;
    }
}
