package jzap.zapcore;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SliceArrayEncoder implements ArrayEncoder{

    List<Object> elems = new ArrayList<>();

    @Override
    public void appendDuration(Duration duration) {
        elems.add(duration);
    }

    @Override
    public void appendTime(Instant time) {
        elems.add(time);
    }

    @Override
    public void appendArray(ArrayMarshaler marshaler) throws ZapException {
        // TODO
    }

    @Override
    public void appendObject(ObjectMarshaler marshaler) throws ZapException {
        // TODO
    }

    @Override
    public void appendReflected(Object object) throws ZapException {
        elems.add(object);
    }

    @Override
    public void appendBool(boolean value) {
        elems.add(value);
    }

    @Override
    public void appendByteString(byte[] value) {
        elems.add(value);
    }

    @Override
    public void appendFloat(float value) {
        elems.add(value);
    }

    @Override
    public void appendDouble(double value) {
        elems.add(value);
    }

    @Override
    public void appendInt(int value) {
        elems.add(value);
    }

    @Override
    public void appendLong(long value) {
        elems.add(value);
    }

    @Override
    public void appendString(String value) {
        elems.add(value);
    }
}
