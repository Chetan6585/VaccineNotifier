package com.opoc.india.vaccine.notifier.cdn.center;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import liquibase.pro.packaged.J;
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
public class Center {

    private String name;
    private String address;
    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("block_name")
    private String tahasil;

    @JsonProperty("pincode")
    private String pincode;

    @JsonProperty("available_capacity")
    private Integer availableCapacity;

    @JsonProperty("min_age_limit")
    private Integer minAgeLimit;

    @JsonProperty("vaccine")
    private String vaccine;

    @JsonProperty("slots")
    private List<String> slots;
}
