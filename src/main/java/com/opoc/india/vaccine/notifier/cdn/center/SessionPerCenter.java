package com.opoc.india.vaccine.notifier.cdn.center;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@Getter
@JsonPOJOBuilder(withPrefix = "")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionPerCenter {

    @JsonProperty("available_capacity")
    private Integer availableCapacity;

    @JsonProperty("min_age_limit")
    private Integer minAgeLimit;

    @JsonProperty("vaccine")
    private String vaccine;

    @JsonProperty("slots")
    private List<String> slots;

    @JsonProperty("date")
    private String date;
}
