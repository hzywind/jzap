package jzap.internal;

@FunctionalInterface
public interface Creator<E> {

    E create();
}
