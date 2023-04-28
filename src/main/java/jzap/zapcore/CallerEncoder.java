package jzap.zapcore;

@FunctionalInterface
public interface CallerEncoder {

    void encode(EntryCaller entry, PrimitiveArrayEncoder encoder);

    CallerEncoder FullCallerEncoder = (ent, enc) -> {
        enc.appendString(ent.toString());
    };

    CallerEncoder ShortCallerEncoder = (ent, enc) -> {
        enc.appendString(ent.trimmedPath());
    };

}
