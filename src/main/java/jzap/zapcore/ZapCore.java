package jzap.zapcore;

import jzap.buffer.Buffer;
import jzap.internal.color.Color;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jzap.internal.color.Color.*;
import static jzap.zapcore.Level.*;
import static jzap.zapcore.NewReflectedEncoder.DefaultNewReflectedEncoder;

public class ZapCore {

    public static final String DefaultLineEnding = "\n";
    public static final String OmitKey = null;
    public static final Clock DefaultClock = new SystemClock();

    static final Map<Level, Color> LevelToColor = Map.of(
            DEBUG, MAGENTA,
            INFO, BLUE,
            WARN, YELLOW,
            ERROR, RED,
            DPANIC, RED,
            PANIC, RED,
            FATAL, RED);
    static final Color UnknownLevelColor = RED;
    static final Map<Level, String> LevelToLowercaseColorString = new HashMap<>();
    static final Map<Level, String> LevelToCapitalColorString = new HashMap<>();
    static {
        for(Map.Entry<Level, Color> e : LevelToColor.entrySet()) {
            LevelToLowercaseColorString.put(e.getKey(), e.getValue().add(e.getKey().toString().toLowerCase()));
            LevelToCapitalColorString.put(e.getKey(), e.getValue().add(e.getKey().toString().toUpperCase()));
        }
    }

    public static Encoder newJSONEncoder(EncoderConfig config) {
        return newJSONEncoder(config, false);
    }

    private static JSONEncoder newJSONEncoder(EncoderConfig cfg, boolean spaced) {
        if (cfg.isSkipLineEnding()) {
            cfg.setLineEnding("");
        } else if (cfg.getLineEnding() == null) {
            cfg.setLineEnding(DefaultLineEnding);
        }

        if (cfg.getNewReflectedEncoder() == null) {
            cfg.setNewReflectedEncoder(DefaultNewReflectedEncoder);
        }

        return new JSONEncoder(cfg, Buffer.get(), spaced);
    }

    public static WriteSyncer addSync(File logFile) {
        return new WriterWrapper(logFile);
    }

    public static WriteSyncer addSync(OutputStream os) {
        return new WriterWrapper(os);
    }

    public static Core newCore(Encoder enc, WriteSyncer writer, Level level) {
            IoCore core = new IoCore();
            core.enab = level;
            core.enc = enc;
            core.out = writer;
            return core;
    }

    public static Core newNopCore() {
        return new Core() {
            @Override
            public Core with(List<Field> fields) {
                return null;
            }

            @Override
            public CheckedEntry check(Entry entry, CheckedEntry ce) {
                return ce;
            }

            @Override
            public void write(Entry entry, Field... fields) throws ZapException {
                // do nothing
            }

            @Override
            public void sync() throws ZapException {
                // do nothing
            }

            @Override
            public boolean enabled(Level level) {
                return false;
            }
        };
    }

    public static Core newTee(Core... cores) {
        switch(cores.length) {
            case 0:
                return newNopCore();
            case 1:
                return cores[0];
            default:
                return new MultiCore(cores);
        }
    }

    public static Encoder newConsoleEncoder(EncoderConfig cfg) {
        if (cfg.getConsoleSeparator() == null) {
            cfg.setConsoleSeparator("\t");
        }
        if (cfg.isSkipLineEnding()) {
            cfg.setLineEnding("");
        } else if (cfg.getLineEnding() == null) {
            cfg.setLineEnding(DefaultLineEnding);
        }

        if (cfg.getNewReflectedEncoder() == null) {
            cfg.setNewReflectedEncoder(DefaultNewReflectedEncoder);
        }
        return new ConsoleEncoder(cfg, Buffer.get(), true);
    }
}


