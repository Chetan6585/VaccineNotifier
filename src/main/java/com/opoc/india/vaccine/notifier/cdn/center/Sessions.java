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
public class Sessions {
    @JsonProperty("sessions")
    private List<Center> centerList;
}
