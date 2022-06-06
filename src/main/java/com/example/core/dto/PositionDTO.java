package com.example.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {
    @JsonProperty("text")
    private String text;

    @JsonProperty("start_index")
    private Integer startIndex;

    @JsonProperty("end_index")
    private Integer endIndex;
}
