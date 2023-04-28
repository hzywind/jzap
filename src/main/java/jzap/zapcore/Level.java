package jzap.zapcore;

public enum Level implements LevelEnabler {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    DPANIC,
    PANIC,
    FATAL;

    public static Level MIN_LEVEL = DEBUG;
    public static Level MAX_LEVEL = FATAL;

    @Override
    public boolean enabled(Level level) {
        return level.compareTo(this) >= 0;
    }
}