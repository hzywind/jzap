package jzap.zapcore;

@FunctionalInterface
public interface NameEncoder {

    void encode(String name, PrimitiveArrayEncoder encoder);

    NameEncoder FullNameEncoder = (name, encoder) -> {
        encoder.appendString(name);
    };
}
