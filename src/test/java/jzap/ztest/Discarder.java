package jzap.ztest;

import jzap.zapcore.WriteSyncer;

import java.io.*;

public class Discarder implements WriteSyncer {

    private PrintWriter writer = new PrintWriter(OutputStream.nullOutputStream());

    @Override
    public void write(String value) {
        //writer.write(value);
    }

    @Override
    public void sync() {
        // TODO add Syncer
    }
}
