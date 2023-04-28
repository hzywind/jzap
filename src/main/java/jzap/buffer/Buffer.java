package jzap.buffer;

import jzap.internal.CommonsPool2Pool;
import jzap.internal.Pool;
import jzap.internal.RingBufferPool;
import jzap.internal.ThreadLocalPool;

public class Buffer {

    private static final int POOL_SIZE = 32;

    private static final Pool<Buffer> RING_BUFFER_POOL = new RingBufferPool<>(POOL_SIZE, Buffer::new);
    private static final Pool<Buffer> COMMONS_POOL2_POOL = new CommonsPool2Pool<>(POOL_SIZE, Buffer::new);
    private static final Pool<Buffer> NOP_POOL = new NopBufferPool();
    private static final Pool<Buffer> THREAD_LOCAL_POOL = new ThreadLocalPool<>(Buffer::new);

    private static final Pool<Buffer> POOL = NOP_POOL;

    private final StringBuilder sb = new StringBuilder(1024);

    public static Buffer get() {
        return POOL.get();
    }

    public void appendChar(char c) {
        sb.append(c);
    }

    public void appendString(String s) {
        sb.append(s);
    }

    public char charAt(int i) {
        return sb.charAt(i);
    }

    public int len() {
        return sb.length();
    }

    public void free() {
        reset();
        POOL.put(this);
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public void appendBool(boolean value) {
        sb.append(value);
    }

    public void appendFloat(float value) {
        sb.append(value);
    }

    public void appendDouble(double value) {
        sb.append(value);
    }

    public void appendLong(long value) {
        sb.append(value);
    }

    public void reset() {
        sb.delete(0, sb.length());
    }

    public void trimNewline() {
        var i = sb.length() - 1;
        if (i > 0 && sb.charAt(i) == '\n') {
            sb.deleteCharAt(i);
        }
    }

    public static class NopBufferPool implements Pool<Buffer> {

        @Override
        public Buffer get() {
            return new Buffer();
        }

        @Override
        public void put(Buffer obj) {
            // do nothing
        }
    }

}
