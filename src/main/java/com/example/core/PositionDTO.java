package com.example.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.validations.RetrieveObjectValidationGroup;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {
    @JsonProperty("start_index")
    @NotNull(groups = {RetrieveObjectValidationGroup.class}, message = "start_index field is missing")
    Integer startIndex;

    @JsonProperty("end_index")
    @NotNull(groups = {RetrieveObjectValidationGroup.class}, message = "end_index field is missing")
    Integer endIndex;
}
