package com.epam.volunteer.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static LocalDateTime formatDateForMySQL(LocalDateTime dateTime) {
        if (dateTime != null) {
            String time = dateTime.toString();
            ZonedDateTime d = ZonedDateTime.parse(time);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
//            dateTime = LocalDateTime.parse(time, formatter);
            return d.toLocalDateTime();
        }
        return null;
    }
}
