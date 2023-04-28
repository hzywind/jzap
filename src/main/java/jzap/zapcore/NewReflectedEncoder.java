package jzap.zapcore;

import jzap.buffer.Buffer;

@FunctionalInterface
public interface NewReflectedEncoder {

    ReflectedEncoder create(Buffer buffer);

    NewReflectedEncoder DefaultNewReflectedEncoder = (buffer) -> {
        return (obj) -> {
            // TODO GSON?
        };
    };
}
