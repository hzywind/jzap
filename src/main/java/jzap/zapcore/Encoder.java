package jzap.zapcore;

import jzap.buffer.Buffer;

public interface Encoder extends ObjectEncoder {

    Buffer encodeEntry(Entry entry, Field... fields) throws ZapException;
}
