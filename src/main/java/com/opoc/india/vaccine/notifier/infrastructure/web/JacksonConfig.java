package com.opoc.india.vaccine.notifier.infrastructure.web;

import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.DurationDeSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.DurationSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalDateDeSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalDateSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalDateTimeDeSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalDateTimeSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalTimeDeSerializer;
import com.opoc.india.vaccine.notifier.infrastructure.web.serializer.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return (Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) -> {
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeSerializer());
            jacksonObjectMapperBuilder.serializerByType(LocalTime.class, new LocalTimeSerializer());
            jacksonObjectMapperBuilder.deserializerByType(LocalTime.class, new LocalTimeDeSerializer());
            jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new LocalDateDeSerializer());
            jacksonObjectMapperBuilder.serializerByType(LocalDate.class, new LocalDateSerializer());
            jacksonObjectMapperBuilder.serializerByType(Duration.class, new DurationSerializer());
            jacksonObjectMapperBuilder.deserializerByType(Duration.class, new DurationDeSerializer());

        };
    }
}
