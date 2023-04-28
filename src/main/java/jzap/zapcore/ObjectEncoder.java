package jzap.zapcore;

import java.time.Duration;
import java.time.Instant;

public interface ObjectEncoder {

    // Logging-specific marshalers.
    void addArray(String key, ArrayMarshaler marshaler) throws ZapException;
    void addObject(String key, ObjectMarshaler marshaler) throws ZapException;

    // Built-in types.
    void addBinary(String key, byte[] value); // for arbitrary bytes
    void addByteString(String key, byte[] value); // for UTF-8 encoded bytes
    void addBool(String key, boolean value);
    void addDuration(String key, Duration value);
    void addFloat(String key, float value);
    void addDouble(String key, double value);
    void addInt(String key, int value);
    void addLong(String key, long value);
    void addString(String key, String value);
    void addTime(String key, Instant time);
    // void AddTime(key string, value time.Time)

    // void AddUintptr(key string, value uintptr)

    // AddReflected uses reflection to serialize arbitrary objects, so it can be
    // slow and allocation-heavy.
    void addReflected(String key, Object object) throws ZapException;

    // OpenNamespace opens an isolated namespace where all subsequent fields will
    // be added. Applications can use namespaces to prevent key collisions when
    // injecting loggers into sub-components or third-party libraries.
    void openNamespace(String key);
}
