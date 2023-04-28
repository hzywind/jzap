package jzap.internal;

public interface Pool<E> {

    E get();

    void put(E obj);
}
