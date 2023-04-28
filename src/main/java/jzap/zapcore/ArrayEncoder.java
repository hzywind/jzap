package jzap.zapcore;

import java.time.Duration;
import java.time.Instant;

public interface ArrayEncoder extends PrimitiveArrayEncoder {
    // Time-related types.
    void appendDuration(Duration duration);
    void appendTime(Instant time);

    // Logging-specific marshalers.
    void appendArray(ArrayMarshaler marshaler) throws ZapException;
    void appendObject(ObjectMarshaler marshaler) throws ZapException;

    // void appendReflected uses reflection to serialize arbitrary objects, so it's
    // slow and allocation-heavy.
    void appendReflected(Object object) throws ZapException;
}
