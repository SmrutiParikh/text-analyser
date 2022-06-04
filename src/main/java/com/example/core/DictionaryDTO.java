package com.example.core;

import com.example.validations.CreateObjectValidationGroup;
import com.example.validations.IdObjectValidationGroup;
import com.example.validations.RetrieveObjectValidationGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryDTO {
    @NotBlank(groups = {IdObjectValidationGroup.class}, message = "id field is missing")
    private String id;

    @JsonProperty("is_case_sensitive")
    @NotNull(groups = {CreateObjectValidationGroup.class}, message = "is_case_sensitive field is missing")
    private Boolean isCaseSensitive;

    @NotNull(groups = {CreateObjectValidationGroup.class}, message = "entries field is missing")
    private List<String> entries;

    @NotNull(groups = {RetrieveObjectValidationGroup.class}, message = "position field is missing")
    private PositionDTO position;
}
