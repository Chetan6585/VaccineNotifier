package com.opoc.india.vaccine.notifier.infrastructure.web.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class LocalDateTimeDeSerializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String localDate = p.getText().trim();
        if (NumberUtils.isNumber(localDate)) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(localDate)),
                    TimeZone.getDefault().toZoneId());
        }
        return LocalDateTime.parse(localDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
