package jzap.zapcore;

import java.time.Duration;

@FunctionalInterface
public interface DurationEncoder {

    void encode(Duration duration, PrimitiveArrayEncoder encoder);

    DurationEncoder SecondsDurationEncoder = (d, e) -> e.appendLong(d.getSeconds());
    DurationEncoder NanosDurationEncoder = (d, e) -> e.appendLong(d.getSeconds() * 1000 * 1000 * 1000 + d.getNano());
    DurationEncoder MillisDurationEncoder = (d, e) -> e.appendLong(d.toMillis());
    DurationEncoder StringDurationEncoder = (d, e) -> e.appendString(d.toString());

}
