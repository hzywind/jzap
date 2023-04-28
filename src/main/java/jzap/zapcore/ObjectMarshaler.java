package jzap.zapcore;

@FunctionalInterface
public interface ObjectMarshaler {

    void marshalLogObject(ObjectEncoder encoder) throws ZapException;

}
