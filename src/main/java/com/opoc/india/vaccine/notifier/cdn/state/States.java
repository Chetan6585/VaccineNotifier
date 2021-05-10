package com.opoc.india.vaccine.notifier.cdn.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@JsonPOJOBuilder(withPrefix = "")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class States {

    @Builder.Default
    @JsonProperty("states")
    private List<State> states = new ArrayList<>();
}
