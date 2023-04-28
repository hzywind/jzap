package jzap.zapcore;

import java.time.Instant;

class SystemClock implements Clock {

    private final java.time.Clock systemClock = java.time.Clock.systemUTC();

    public Instant now() {
        return systemClock.instant();
    }
}
