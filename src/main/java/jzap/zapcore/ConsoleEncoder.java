package jzap.zapcore;

import jzap.buffer.Buffer;
import jzap.internal.Pool;
import jzap.internal.ThreadLocalPool;

import static jzap.zapcore.NameEncoder.FullNameEncoder;

class ConsoleEncoder extends JSONEncoder {

    private static final Pool<SliceArrayEncoder> _sliceEncoderPool = new ThreadLocalPool<>(SliceArrayEncoder::new);

    private static SliceArrayEncoder getSliceEncoder() {
        return _sliceEncoderPool.get();
    }

    private static void putSliceEncoder(SliceArrayEncoder e) {
        e.elems.clear();
        _sliceEncoderPool.put(e);
    }

    private final EncoderConfig cfg;

    public ConsoleEncoder(EncoderConfig cfg, Buffer buf, boolean spaced) {
        super(cfg, buf, spaced);
        this.cfg = cfg;
    }

    @Override
    public Buffer encodeEntry(Entry ent, Field... fields) throws ZapException {
        var line = Buffer.get();

        // TODO -> var
        SliceArrayEncoder arr = getSliceEncoder();

        if (cfg.getTimeKey() != null && cfg.getEncodeTime() != null) {
            cfg.getEncodeTime().encode(ent.getTime(), arr);
        }
        if (cfg.getLevelKey() != null && cfg.getEncodeLevel() != null) {
            cfg.getEncodeLevel().encode(ent.getLevel(), arr);
        }
        if (ent.getLoggerName() != null && cfg.getNameKey() != null) {
            var nameEncoder = cfg.getEncodeName();

            if (nameEncoder == null) {
                // Fall back to FullNameEncoder for backward compatibility.
                nameEncoder = FullNameEncoder;
            }

            nameEncoder.encode(ent.getLoggerName(), arr);
        }
        if (ent.getCaller() != null) {
            if (cfg.getCallerKey() != null && cfg.getEncodeCaller() != null) {
                cfg.getEncodeCaller().encode(ent.getCaller(), arr);
            }
            if (cfg.getFunctionKey() != null) {
                arr.appendString(ent.getCaller().getFunction());
            }
        }
        for (var i = 0; i < arr.elems.size(); i++) {
            if (i > 0) {
                line.appendString(cfg.getConsoleSeparator());
            }
            line.appendString(arr.elems.get(i).toString());
        }
        putSliceEncoder(arr);

        // Add the message itself.
        if (cfg.getMessageKey() != null) {
            addSeparatorIfNecessary(line);
            line.appendString(ent.getMessage());
        }

        // Add any structured context.
        writeContext(line, fields);

        // If there's no stacktrace key, honor that; this allows users to force
        // single-line output.
        if (ent.getStack() != null && cfg.getStacktraceKey() != null) {
            line.appendChar('\n');
            line.appendString(ent.getStack());
        }

        line.appendString(cfg.getLineEnding());
        return line;
    }

    private void writeContext(Buffer line, Field[] extra) {
        // TODO fields + more
    }

    private void addSeparatorIfNecessary(Buffer line) {
        if (line.len() > 0) {
            line.appendString(cfg.getConsoleSeparator());
        }
    }
}
