package com.epam.volunteer.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mikita Buslauski
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy'T'HH:mm";

    public LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        LocalDateTime time = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(),
                localDateTime.getSecond());
        jsonGenerator.writeObject(time.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    }
}
