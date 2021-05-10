package com.opoc.india.vaccine.notifier.cdn.state;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonPOJOBuilder(withPrefix = "")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Districts {
    @Builder.Default
    private List<District> districts= new ArrayList<>();
}
