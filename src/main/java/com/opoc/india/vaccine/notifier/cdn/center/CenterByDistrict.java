package com.opoc.india.vaccine.notifier.cdn.center;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Data
@Getter
@JsonPOJOBuilder(withPrefix = "")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CenterByDistrict {

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

    @JsonProperty("sessions")
    @Setter
    private List<SessionPerCenter> sessionPerCenter;
}
