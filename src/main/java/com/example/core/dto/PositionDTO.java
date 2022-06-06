package com.example.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {
    @JsonProperty("start_index")
    Integer startIndex;

    @JsonProperty("end_index")
    Integer endIndex;
}
