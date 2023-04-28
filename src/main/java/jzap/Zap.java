package jzap;

import jzap.buffer.Buffer;
import jzap.zapcore.*;

import java.util.Arrays;

public class Zap {

    public static EncoderConfig newProductionEncoderConfig() {
        var cfg = new EncoderConfig();
        cfg.setTimeKey("ts");
        cfg.setLevelKey("level");
        cfg.setNameKey("logger");
        cfg.setCallerKey("caller");
        cfg.setFunctionKey(ZapCore.OmitKey);
        cfg.setMessageKey("msg");
        cfg.setStacktraceKey("stacktrace");
        cfg.setLineEnding(ZapCore.DefaultLineEnding);
        cfg.setEncodeLevel(LevelEncoder.LowercaseLevelEncoder);
        cfg.setEncodeTime(TimeEncoder.EpocMilliTimeEncoder);
        cfg.setEncodeDuration(DurationEncoder.SecondsDurationEncoder);
        cfg.setEncodeCaller(CallerEncoder.ShortCallerEncoder);
        return cfg;
    }

    public static Logger newLogger(Core core, Option... options) {
        if (core == null) {
            return newNopLogger();
        }
        var log = new Logger(core, ZapCore.DefaultClock);
        log.errorOutput = ZapCore.addSync(System.err);
        return log.withOptions(options);
    }

    public static Logger newNopLogger() {
        return new Logger(ZapCore.newNopCore(), ZapCore.DefaultClock);
    }

    public static Option addStacktrace(Level level) {
        return (logger) -> {
            logger.addStack = level;
        };
    }

    public static Option addCaller() {
        return withCaller(true);
    }

    public static Option withCaller(boolean enabled) {
        return (logger) -> {
            logger.addCaller = enabled;
        };
    }

    protected static Stacktrace captureStacktrace(int skip, StacktraceDepth depth) {
        var stacktraceElems = Arrays.asList(Thread.currentThread().getStackTrace());
        var toIndex = skip + 1;
        if (depth == StacktraceDepth.FULL) {
            toIndex = stacktraceElems.size();
        }
        return new Stacktrace(stacktraceElems.subList(skip, toIndex));
    }

    protected static StackFormatter newStackFormatter(Buffer b) {
        return new StackFormatter(b);
    }
}
