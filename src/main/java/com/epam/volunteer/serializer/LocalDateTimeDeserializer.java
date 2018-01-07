package com.epam.volunteer.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mikita Buslauski
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy'T'HH:mm";

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}

