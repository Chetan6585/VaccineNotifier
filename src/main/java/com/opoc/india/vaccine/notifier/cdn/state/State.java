package com.opoc.india.vaccine.notifier.cdn.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Data
@Builder
@JsonPOJOBuilder(withPrefix = "")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class State {

    @JsonProperty("state_id")
    private Integer stateId;
    @JsonProperty("state_name")
    private String stateName;

}
