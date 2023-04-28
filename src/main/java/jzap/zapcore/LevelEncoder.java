package jzap.zapcore;

import static jzap.zapcore.ZapCore.*;

@FunctionalInterface
public interface LevelEncoder {

    void encode(Level level, PrimitiveArrayEncoder encoder);

    LevelEncoder LowercaseLevelEncoder = (lvl, encoder) -> {
        encoder.appendString(lvl.toString().toLowerCase());
    };

    LevelEncoder LowercaseColorLevelEncoder = (lvl, encoder) -> {
        String s = LevelToLowercaseColorString.get(lvl);
        if (s == null) {
            s = UnknownLevelColor.add(lvl.name().toLowerCase());
        }
        encoder.appendString(s);
    };

    LevelEncoder CapitalLevelEncoder = (lvl, encoder) -> {
        encoder.appendString(lvl.toString().toUpperCase());
    };

    LevelEncoder CapitalColorLevelEncoder = (lvl, encoder) -> {
        String s = LevelToCapitalColorString.get(lvl);
        if (s == null) {
            s = UnknownLevelColor.add(lvl.name().toUpperCase());
        }
        encoder.appendString(s);
    };

}
