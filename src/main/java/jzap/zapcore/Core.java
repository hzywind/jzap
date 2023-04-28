package jzap.zapcore;

import jzap.buffer.Buffer;

import java.util.List;

import static jzap.zapcore.Level.ERROR;

public interface Core extends LevelEnabler {

    Core with(List<Field> fields);

    CheckedEntry check(Entry entry, CheckedEntry ce);

    void write(Entry entry, Field... fields) throws ZapException;

    void sync() throws ZapException;
}

class IoCore implements Core {

    LevelEnabler enab;
    Encoder enc;
    WriteSyncer out;

    @Override
    public Core with(List<Field> fields) {
        return null;
    }

    @Override
    public CheckedEntry check(Entry ent, CheckedEntry ce) {
        if (enabled(ent.getLevel())) {
            if (ce == null) {
                ce = CheckedEntry.getCheckedEntry();
            }
            ce.setEntry(ent);
            ce.addCore(ent, this);
        }
        return ce;
    }

    @Override
    public void write(Entry entry, Field... fields) throws ZapException {
        Buffer buf = null;
        try {
            buf = enc.encodeEntry(entry, fields);
            out.write(buf.toString());
        } finally {
            if (buf != null) {
                buf.free();
            }
        }
        if (entry.getLevel().compareTo(ERROR) > 0) {
            sync();
        }
    }

    @Override
    public void sync() throws ZapException {
        out.sync();
    }

    @Override
    public boolean enabled(Level level) {
        return enab.enabled(level);
    }
}
