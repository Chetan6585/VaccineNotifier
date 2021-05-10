package com.opoc.india.vaccine.notifier.cdn.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@JsonPOJOBuilder(withPrefix = "")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class District {
    @JsonProperty("district_id")
    private Integer districtId;
    @JsonProperty("district_name")
    private String districtName;
}
