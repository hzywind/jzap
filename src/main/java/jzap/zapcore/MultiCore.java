package jzap.zapcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MultiCore extends ArrayList<Core> implements Core {

    public MultiCore(Core[] cores) {
        super(Arrays.asList(cores));
    }

    @Override
    public Core with(List<Field> fields) {
        for (var c : this) {
            c.with(fields);
        }
        return this;
    }

    @Override
    public CheckedEntry check(Entry entry, CheckedEntry ce) {
        for (var c : this) {
            ce = c.check(entry, ce);
        }
        return ce;
    }

    @Override
    public void write(Entry entry, Field... fields) throws ZapException {
        var exp = new MultiZapException();
        for (var c : this) {
            try {
                c.write(entry, fields);
            } catch (ZapException e) {
                exp.append(e);
            }
        }
        if (!exp.isEmpty()) {
            throw exp;
        }
    }

    @Override
    public void sync() throws ZapException {
        var exp = new MultiZapException();
        for (var c : this) {
            try {
                c.sync();
            } catch (ZapException e) {
                exp.append(e);
            }
        }
        if (!exp.isEmpty()) {
            throw exp;
        }
    }

    @Override
    public boolean enabled(Level level) {
        for (var c : this) {
            if (c.enabled(level)) {
                return true;
            }
        }
        return false;
    }
}
