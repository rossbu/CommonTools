package com.demo;
import java.time.*;

public class TimeDemo {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();

        // Yield correct result.
        System.out.println("useLocalDateTime -> " + useLocalDateTime(currentTimeMillis));

        // Also yield correct result.
        System.out.println("useZonedDateTime -> " + useZonedDateTime(currentTimeMillis));
    }

    public static DayOfWeek useLocalDateTime(long currentTimeMillis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(currentTimeMillis),
                ZoneId.systemDefault()
        );

        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();

        return dayOfWeek;
    }

    public static DayOfWeek useZonedDateTime(long currentTimeMillis) {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.systemDefault());

        DayOfWeek dayOfWeek = zonedDateTime.getDayOfWeek();

        return dayOfWeek;
    }
}
