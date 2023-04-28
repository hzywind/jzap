package jzap.zapcore;

import jzap.buffer.Buffer;

public class EntryCaller {

    public EntryCaller (StackTraceElement trace) {
        file = trace.getFileName();
        line = trace.getLineNumber();
        function = trace.getMethodName();
        className = trace.getClassName();
    }

    private String file;
    private int line;
    private String function;
    private String className;

    public String fullPath() {
        var buf = Buffer.get();
        buf.appendString(file);
        buf.appendChar(':');
        buf.appendLong(line);
        var caller = buf.toString();
        buf.free();
        return caller;
    }

    public String trimmedPath() {
        // TODO
        return fullPath();
    }

    public String getFunction() {
        return function;
    }

    public String toString() {
        return fullPath();
    }
}
