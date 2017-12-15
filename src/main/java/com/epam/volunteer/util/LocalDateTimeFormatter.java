package com.epam.volunteer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private LocalDateTimeFormatter() {}
    public static LocalDateTime formatDateForMySQL(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            dateTime.format(formatter);
            return dateTime;
        }
        return null;
    }
}
