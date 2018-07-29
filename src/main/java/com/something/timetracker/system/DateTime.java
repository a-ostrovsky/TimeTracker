package com.something.timetracker.system;

import java.time.Clock;
import java.time.LocalDateTime;

public final class DateTime {
    private static Clock clock = Clock.systemDefaultZone();

    public static void setClock(Clock clock) {
        DateTime.clock = clock;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}
