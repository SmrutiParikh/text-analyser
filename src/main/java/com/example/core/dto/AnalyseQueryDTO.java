package com.example.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseQueryDTO {
    @JsonProperty("dictionary_id")
    @NotBlank(message = "dictionary_id field is missing")
    String dictionaryId;

    @NotBlank(message = "target field is missing")
    String target;
}
