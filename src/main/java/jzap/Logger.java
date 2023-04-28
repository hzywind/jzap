package jzap;

import jzap.buffer.Buffer;
import jzap.zapcore.*;

public class Logger {

    private Core core;
    private boolean development;
    protected boolean addCaller;

    private String name;
    protected WriteSyncer errorOutput;
    protected LevelEnabler addStack;
    private int callerSkip;
    private Clock clock;

    public Logger(Core core, Clock clock) {
        this.core = core;
        this.clock = clock;
    }

    public void info(String msg, Field... fields) {
        CheckedEntry ce = check(Level.INFO, msg);
        if (ce != null) {
            ce.write(fields);
        }
    }

    public void error(String msg, Field... fields) {
        CheckedEntry ce = check(Level.ERROR, msg);
        if (ce != null) {
            ce.write(fields);
        }
    }

    private CheckedEntry check(Level lvl, String msg) {
        final int callerSkipOffset = 4;

        if (lvl.compareTo(Level.DPANIC) < 0 && !core.enabled(lvl)) {
            return null;
        }

        var ent = new Entry(lvl, clock.now(), name, msg);
        var ce = core.check(ent, null);
        var willWrite = ce != null;

        // TODO Set up any required terminal behavior.

        if (!willWrite) {
            return ce;
        }

        ce.setErrorOutput(errorOutput);

        var addStack = this.addStack.enabled(lvl);
        if (!this.addCaller && !addStack) {
            return ce;
        }

        var stackDepth = StacktraceDepth.FIRST;
        if (addStack) {
            stackDepth = StacktraceDepth.FULL;
        }
        var stack = Zap.captureStacktrace(callerSkip + callerSkipOffset, stackDepth);

        if (stack.count() == 0) {
            if (addCaller) {
                errorOutput.write(String.format("%s Logger.check error: failed to get caller\n", ent.getTime().toString()));
                errorOutput.sync();
            }
        }

        var frame = stack.next();
        if (addCaller) {
            ce.getEntry().setCaller(new EntryCaller(frame));
        }

        if (addStack) {
            var buffer = Buffer.get();
            try {
                var stackfmt = Zap.newStackFormatter(buffer);
                stackfmt.formatFrame(frame);
                if (stack.hasNext()) {
                    stackfmt.formatStack(stack);
                }
                ce.getEntry().setStack(stackfmt.toString());
            } finally {
                // TODO by closeable?
                buffer.free();
            }
        }

        return ce;
    }

    public Logger withOptions(Option[] options) {
        for(var opt:options) {
            opt.apply(this);
        }
        return this;
    }
}
