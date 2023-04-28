package jzap.zapcore;

public class EncoderConfig {

    private String messageKey;
    private String levelKey;
    private String timeKey;
    private String nameKey;
    private String callerKey;
    private String functionKey;
    private String stacktraceKey;
    private boolean skipLineEnding;
    private String lineEnding;

    private LevelEncoder encodeLevel;
    private TimeEncoder encodeTime;
    private DurationEncoder encodeDuration;
    private CallerEncoder encodeCaller;

    private NameEncoder encodeName;

    private NewReflectedEncoder newReflectedEncoder;

    private String consoleSeparator;

    public String getLineEnding() {
        return lineEnding;
    }

    public void setLineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
    }

    public TimeEncoder getEncodeTime() {
        return encodeTime;
    }

    public void setEncodeTime(TimeEncoder encodeTime) {
        this.encodeTime = encodeTime;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getLevelKey() {
        return levelKey;
    }

    public void setLevelKey(String levelKey) {
        this.levelKey = levelKey;
    }

    public String getTimeKey() {
        return timeKey;
    }

    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getCallerKey() {
        return callerKey;
    }

    public void setCallerKey(String callerKey) {
        this.callerKey = callerKey;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public String getStacktraceKey() {
        return stacktraceKey;
    }

    public void setStacktraceKey(String stacktraceKey) {
        this.stacktraceKey = stacktraceKey;
    }

    public boolean isSkipLineEnding() {
        return skipLineEnding;
    }

    public void setSkipLineEnding(boolean skipLineEnding) {
        this.skipLineEnding = skipLineEnding;
    }

    public DurationEncoder getEncodeDuration() {
        return encodeDuration;
    }

    public void setEncodeDuration(DurationEncoder encodeDuration) {
        this.encodeDuration = encodeDuration;
    }

    public NewReflectedEncoder getNewReflectedEncoder() {
        return newReflectedEncoder;
    }

    public void setNewReflectedEncoder(NewReflectedEncoder func) {
        this.newReflectedEncoder = func;
    }

    public LevelEncoder getEncodeLevel() {
        return encodeLevel;
    }

    public void setEncodeLevel(LevelEncoder encodeLevel) {
        this.encodeLevel = encodeLevel;
    }

    public CallerEncoder getEncodeCaller() {
        return encodeCaller;
    }

    public void setEncodeCaller(CallerEncoder encodeCaller) {
        this.encodeCaller = encodeCaller;
    }

    public NameEncoder getEncodeName() {
        return encodeName;
    }

    public void setEncodeName(NameEncoder encodeName) {
        this.encodeName = encodeName;
    }

    public String getConsoleSeparator() {
        return consoleSeparator;
    }

    public void setConsoleSeparator(String consoleSeparator) {
        this.consoleSeparator = consoleSeparator;
    }
}
