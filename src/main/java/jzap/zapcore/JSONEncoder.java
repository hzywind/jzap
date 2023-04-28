package jzap.zapcore;

import jzap.buffer.Buffer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

public class JSONEncoder implements Encoder, ArrayEncoder {

    private final boolean spaced;
    private final EncoderConfig cfg;
    private final Buffer buf;
    private int openNamespaces;

    private Buffer reflectBuf;
    private ReflectedEncoder reflectEnc;


    public JSONEncoder(EncoderConfig cfg, Buffer buf, boolean spaced) {
        this.cfg = cfg;
        this.buf = buf;
        this.spaced = spaced;
    }

    private static JSONEncoder clone(JSONEncoder enc) {
        // TODO pool
        var ret = new JSONEncoder(enc.cfg, Buffer.get(), enc.spaced);
        ret.openNamespaces = enc.openNamespaces;
        return ret;
    }

    @Override
    public void addArray(String key, ArrayMarshaler marshaler) throws ZapException {
        addKey(key);
        appendArray(marshaler);
    }

    private void addKey(String key) {
        addElementSeparator();
        buf.appendChar('"');
        safeAddString(key);
        buf.appendChar('"');
        buf.appendChar(':');
        if (spaced) {
            buf.appendChar(' ');
        }
    }

    private void safeAddString(String key) {
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    buf.appendChar('\\');
                    buf.appendChar(c);
                    break;
                case '\n':
                    buf.appendChar('\\');
                    buf.appendChar('n');
                    break;
                case '\r':
                    buf.appendChar('\\');
                    buf.appendChar('r');
                    break;
                case '\t':
                    buf.appendChar('\\');
                    buf.appendChar('t');
                    break;
                default:
                    buf.appendChar(c);
            }
        }
    }

    private void addElementSeparator() {
        var last = buf.len() - 1;
        if (last < 0) {
            return;
        }
        switch (buf.charAt(last)) {
            case '{':
            case '[':
            case ':':
            case ',':
            case ' ':
                return;
            default:
                buf.appendChar(',');
                if (spaced) {
                    buf.appendChar(' ');
                }
        }
    }

    @Override
    public void addObject(String key, ObjectMarshaler obj) throws ZapException {
        addKey(key);
        appendObject(obj);
    }

    @Override
    public void addBinary(String key, byte[] value) {
        addKey(key);
        appendString(Base64.getEncoder().encodeToString(value));
    }

    @Override
    public void addByteString(String key, byte[] value) {
        addKey(key);
        appendByteString(value);
    }

    @Override
    public void addBool(String key, boolean value) {
        addKey(key);
        appendBool(value);
    }

    @Override
    public void addDuration(String key, Duration value) {
        addKey(key);
        appendDuration(value);
    }

    @Override
    public void addFloat(String key, float value) {
        addKey(key);
        appendFloat(value);
    }

    @Override
    public void addDouble(String key, double value) {
        addKey(key);
        appendDouble(value);
    }

    @Override
    public void addInt(String key, int value) {
        addKey(key);
        appendInt(value);
    }

    @Override
    public void addLong(String key, long value) {
        addKey(key);
        appendLong(value);
    }

    @Override
    public void addString(String key, String value) {
        addKey(key);
        appendString(value);
    }

    @Override
    public void addTime(String key, Instant time) {
        addKey(key);
        appendTime(time);
    }

    @Override
    public void addReflected(String key, Object object) throws ZapException {
        String value = encodeReflected(object);
        addKey(key);
        buf.appendString(value);
    }

    @Override
    public void openNamespace(String key) {
        addKey(key);
        buf.appendChar('{');
        openNamespaces++;
    }

    private void closeOpenNamespaces() {
        for (var i = 0; i < openNamespaces; i++) {
            buf.appendChar('}');
        }
        openNamespaces = 0;
    }

    @Override
    public void appendDuration(Duration duration) {
        var cur = buf.len();
        var e = cfg.getEncodeDuration();
        if (e != null) {
            e.encode(duration, this);
        }
        if (cur == buf.len()) {
            appendLong(duration.toMillis());
        }
    }

    @Override
    public void appendTime(Instant time) {
        var cur = buf.len();
        var e = cfg.getEncodeTime();
        if (e != null) {
            e.encode(time, this);
        }
        if (cur == buf.len()) {
            appendLong(time.toEpochMilli());
        }
    }

    @Override
    public void appendArray(ArrayMarshaler marshaler) throws ZapException {
        addElementSeparator();
        buf.appendChar('[');
        marshaler.marshalLogArray(this);
        buf.appendChar(']');
    }

    @Override
    public void appendObject(ObjectMarshaler marshaler) throws ZapException {
        var old = openNamespaces;
        openNamespaces = 0;
        addElementSeparator();
        buf.appendChar('{');
        marshaler.marshalLogObject(this);
        buf.appendChar('}');
        closeOpenNamespaces();
        openNamespaces = old;
    }

    @Override
    public void appendReflected(Object object) throws ZapException {
        String value = encodeReflected(object);
        addElementSeparator();
        buf.appendString(value);
    }

    private static final String NULL_LITERAL = "null";

    private String encodeReflected(Object object) throws ZapException {
        if (object == null)
            return NULL_LITERAL;
        resetReflectBuf();
        reflectEnc.encode(object);
        reflectBuf.trimNewline();
        return reflectBuf.toString();
    }

    private void resetReflectBuf() {
        if (reflectBuf == null) {
            reflectBuf = Buffer.get();
            reflectEnc = cfg.getNewReflectedEncoder().create(reflectBuf);
        } else {
            reflectBuf.reset();
        }
    }

    @Override
    public void appendBool(boolean value) {
        addElementSeparator();
        buf.appendBool(value);
    }

    @Override
    public void appendByteString(byte[] value) {
        addElementSeparator();
        buf.appendChar('"');
        safeAddString(new String(value, StandardCharsets.UTF_8));
        buf.appendChar('"');
    }

    @Override
    public void appendFloat(float value) {
        addElementSeparator();
        if (Float.isInfinite(value)) {
            if (value > 0) {
                buf.appendString("+Inf");
            } else {
                buf.appendString("-Inf");
            }
        } else if (Float.isNaN(value)) {
            buf.appendString("NaN");
        } else {
            buf.appendFloat(value);
        }
    }

    @Override
    public void appendDouble(double value) {
        addElementSeparator();
        if (Double.isInfinite(value)) {
            if (value > 0) {
                buf.appendString("+Inf");
            } else {
                buf.appendString("-Inf");
            }
        } else if (Double.isNaN(value)) {
            buf.appendString("NaN");
        } else {
            buf.appendDouble(value);
        }
    }

    @Override
    public void appendInt(int value) {
        appendLong((long) value);
    }

    @Override
    public void appendLong(long value) {
        addElementSeparator();
        buf.appendLong(value);
    }

    @Override
    public void appendString(String value) {
        addElementSeparator();
        buf.appendChar('"');
        safeAddString(value);
        buf.appendChar('"');
    }

    @Override
    public Buffer encodeEntry(Entry ent, Field... fields) throws ZapException {
        var enc = clone(this);
        var cfg = enc.cfg;

        enc.buf.appendChar('{');

        if (cfg.getLevelKey() != null && cfg.getEncodeLevel() != null) {
            enc.addKey(cfg.getLevelKey());
            var cur = enc.buf.len();
            cfg.getEncodeLevel().encode(ent.getLevel(), enc);
            if (cur == enc.buf.len()) {
                enc.appendString(ent.getLevel().name());
            }
        }

        if (cfg.getTimeKey() != null) {
            enc.addTime(cfg.getTimeKey(), ent.getTime());
        }

        if (ent.getLoggerName() != null && cfg.getNameKey() != null) {
            enc.addKey(cfg.getNameKey());
            var cur = enc.buf.len();
            var nameEncoder = cfg.getEncodeName();
            if (nameEncoder == null) {
                nameEncoder = NameEncoder.FullNameEncoder;
            }
            nameEncoder.encode(ent.getLoggerName(), enc);
            if (cur == enc.buf.len()) {
                enc.appendString(ent.getLoggerName());
            }
        }

        if (ent.getCaller() != null) {
            if (cfg.getCallerKey() != null) {
                enc.addKey(cfg.getCallerKey());
                var cur = enc.buf.len();
                cfg.getEncodeCaller().encode(ent.getCaller(), enc);
                if (cur == enc.buf.len()) {
                    enc.appendString(ent.getCaller().toString());
                }
            }
            if (cfg.getFunctionKey() != null) {
                enc.addKey(cfg.getFunctionKey());
                enc.appendString(ent.getCaller().getFunction());
            }
        }

        if (cfg.getMessageKey() != null) {
            enc.addKey(cfg.getMessageKey());
            enc.appendString(ent.getMessage());
        }

        // TODO fields + more

        if (ent.getStack() != null && cfg.getStacktraceKey() != null) {
            enc.addString(cfg.getStacktraceKey(), ent.getStack());
        }

        enc.buf.appendChar('}');
        enc.buf.appendString(cfg.getLineEnding());

        return enc.buf;
    }
}
