package jzap;

import jzap.buffer.Buffer;

class StackFormatter {

    private final Buffer b;
    private boolean nonEmpty;

    StackFormatter(Buffer b) {
        this.b = b;
    }

    public void formatFrame(StackTraceElement frame) {
        if (nonEmpty) {
            b.appendChar('\n');
        }
        nonEmpty = true;
        b.appendString(frame.toString());
    }

    public void formatStack(Stacktrace stack) {
        while(stack.hasNext()) {
            formatFrame(stack.next());
        }
    }

    @Override
    public String toString() {
        return b.toString();
    }
}
