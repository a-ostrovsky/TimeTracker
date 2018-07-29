package helpers;

import com.something.timetracker.system.DateTime;

import java.time.*;

public final class TimeMachine {
    private static long currentEpochSeconds = 0;

    public static void use() {
        setDateTime(LocalDateTime.now().withNano(0));
        setTimeToCurrentEpochSeconds();
    }

    public static LocalDateTime addTime(Duration duration) {
        currentEpochSeconds += duration.getSeconds();
        setTimeToCurrentEpochSeconds();
        return DateTime.now();
    }

   public static void setDateTime(LocalDateTime dateTime) {
        currentEpochSeconds = dateTime.toEpochSecond(ZoneOffset.UTC);
        setTimeToCurrentEpochSeconds();
    }

        private static void setTimeToCurrentEpochSeconds() {
        Clock clock = Clock.fixed(Instant.ofEpochSecond(currentEpochSeconds),
                ZoneId.of("Z"));
        DateTime.setClock(clock);
        verifySecondPartIsZero();
    }

    private static void verifySecondPartIsZero() {
        LocalDateTime now = DateTime.now();
        if (now.getNano() != 0) {
            throw new IllegalStateException(String.format("Nanosecond parts are " +
                    "ignored throughout the entire application. So setting such time values is " +
                    "not supported. Currently set time is: %s", now));
        }
    }
}
