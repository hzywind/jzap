package jzap.zapcore;

@FunctionalInterface
public interface ReflectedEncoder {

    void encode(Object obj) throws ZapException;

}
