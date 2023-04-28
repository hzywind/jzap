package jzap.zapcore;

public interface PrimitiveArrayEncoder {

    void appendBool(boolean value);
    void appendByteString(byte[] value); // for UTF-8 encoded bytes
    void appendFloat(float value);
    void appendDouble(double value);
    void appendInt(int value);
    void appendLong(long value);
    void appendString(String value);
}
