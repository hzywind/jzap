package jzap.zapcore;

import java.io.*;

public interface WriteSyncer {

    void write(String value);

    void sync();
}

class WriterWrapper implements WriteSyncer {
    private PrintStream writer;

    WriterWrapper(File logFile) {
        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            init(fos);
        } catch (FileNotFoundException e) {
            // todo
            e.printStackTrace();
        }
    }

    WriterWrapper(OutputStream os) {
        init(os);
    }

    private void init(OutputStream os) {
        if (os instanceof PrintStream) {
            writer = (PrintStream) os;
        } else {
            writer = new PrintStream(os, true);
        }
        if (os != System.out && os != System.err) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    if (writer != null) {
                        sync();
                        writer.close();
                    }
                }
            });
        }
    }

    @Override
    public void write(String value) {
        if (writer != null) {
            writer.print(value);
        }
    }

    @Override
    public void sync() {
        if (writer != null) {
            writer.flush();
        }
    }
}