package jzap.internal;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class RingBufferPool<E> implements Pool<E> {

    private final int mod;
    private final AtomicReferenceArray<E> pool;
    private final AtomicLong producerIdx, consumerIdx = new AtomicLong(0);

    public RingBufferPool(int size, Creator<E> creator) {
        this.mod = size - 1;
        final Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = creator.create();
        }
        this.pool = new AtomicReferenceArray(arr);
        this.producerIdx = new AtomicLong(size);
    }

    @Override
    public E get() {
        var idx = (int)(consumerIdx.getAndIncrement() & mod);
        E e = pool.getAndSet(idx, null);
        if (e == null) {
            // e is retrieved by someone else after idx is calculated and before getAndSet is invoked
            return get();
        }
        return e;
    }

    @Override
    public void put(E obj) {
        var idx = (int)(producerIdx.getAndIncrement() & mod);
        E e = pool.getAndSet(idx, obj);
        if (e != null) {
            // e is put back by someone else after idx is calculated and before getAndSet is invoked
            put(e);
        }
    }
}
