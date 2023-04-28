package jzap.internal;

/**
 * Be careful to use ThreadLocal as it is unbounded
 * and can cause memory leak if the thread is not recycled.
 */
public class ThreadLocalPool<E> implements Pool<E> {

    private final ThreadLocal<E> threadLocal;

    public ThreadLocalPool(Creator<E> c) {
        threadLocal = ThreadLocal.withInitial(c::create);
    }

    @Override
    public E get() {
        return threadLocal.get();
    }

    @Override
    public void put(E obj) {
        // do nothing
    }

}
