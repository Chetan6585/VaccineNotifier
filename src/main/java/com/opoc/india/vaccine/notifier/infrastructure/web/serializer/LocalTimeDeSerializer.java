package com.opoc.india.vaccine.notifier.infrastructure.web.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeSerializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String localTime = p.getText().trim();
        return LocalTime.parse(localTime, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
