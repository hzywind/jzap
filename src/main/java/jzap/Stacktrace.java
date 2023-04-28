package jzap;

import java.util.Iterator;
import java.util.List;

public class Stacktrace {

    private final Iterator<StackTraceElement> frames;
    private final int size;

    public Stacktrace(List<StackTraceElement> stack) {
            size = stack.size();
            frames = stack.iterator();
    }

    public int count() {
        return size;
    }

    public StackTraceElement next() {
        return frames.next();
    }

    public boolean hasNext() {
        return frames.hasNext();
    }
}
