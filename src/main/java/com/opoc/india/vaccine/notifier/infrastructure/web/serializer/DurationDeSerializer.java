package com.opoc.india.vaccine.notifier.infrastructure.web.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.time.Duration;

public class DurationDeSerializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if(NumberUtils.isNumber(p.getText())) {
            return Duration.ofSeconds(Long.parseLong(p.getText()));
        } else {
            return Duration.parse(p.getText());
        }
    }
}
