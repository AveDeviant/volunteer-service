package com.epam.volunteer.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mikita Buslauski
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy'T'HH:mm";

    public LocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}

