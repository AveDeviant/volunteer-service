package com.epam.volunteer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DTOLocalDateTimeConverter {
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy'T'HH:mm";

    private DTOLocalDateTimeConverter() {
    }

    public static String convertToString(LocalDateTime dateTime) {
        if (dateTime != null) {
            LocalDateTime time = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(),
                    dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
            return time.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        }
        return "";
    }
}
