package jzap.zapcore;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@FunctionalInterface
public interface TimeEncoder {

    void encode(Instant time, PrimitiveArrayEncoder encoder);

    DateTimeFormatter ISO8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneId.systemDefault());

    TimeEncoder EpocTimeEncoder = (time, encoder) -> encoder.appendLong(time.getEpochSecond());
    TimeEncoder EpocMilliTimeEncoder = (time, encoder) -> encoder.appendLong(time.toEpochMilli());
    TimeEncoder ISO8601TimeEncoder = (time, encoder) -> encoder.appendString(ISO8601.format(time));

}
