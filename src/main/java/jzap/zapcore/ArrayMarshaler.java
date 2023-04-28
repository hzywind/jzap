package jzap.zapcore;

@FunctionalInterface
public interface ArrayMarshaler {

    void marshalLogArray(ArrayEncoder encoder);

}
