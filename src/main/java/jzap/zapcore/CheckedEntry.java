package jzap.zapcore;

import java.util.ArrayList;
import java.util.List;

public class CheckedEntry {

    private Entry entry;
    private WriteSyncer errorOutput;
    private List<Core> cores = new ArrayList<>();


    public static CheckedEntry getCheckedEntry() {
        // TODO use pool
        return new CheckedEntry();
    }

    public void setErrorOutput(WriteSyncer errorOutput) {
        this.errorOutput = errorOutput;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public void write(Field[] fields) {

        // TODO check dirty

        ZapException err = null;
        for (Core c : cores) {
            try {
                c.write(entry, fields);
            } catch (ZapException e) {
                // TODO multi err
                err = e;
                break;
            }
        }
        if (err != null && errorOutput != null) {
            errorOutput.write(String.format("%v write error: %v\n", entry.getTime(), err));
        }

        // TODO handle "should"

    }

    public CheckedEntry addCore(Entry ent, Core core) {
        cores.add(core);
        return this;
    }
}
