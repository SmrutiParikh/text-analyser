package com.example.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseQueryDTO {
    @JsonProperty("dictionary_id")
    @NotBlank(message = "dictionary_id is missing")
    String dictionaryId;

    @NotBlank(message = "target is missing")
    String target;
}
