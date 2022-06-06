package com.example.core.dto;

import com.example.core.ValidationGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@JsonDeserialize
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryDTO {
    @NotBlank(groups = {ValidationGroup.IdObjectValidationGroup.class}, message = "id field is missing")
    private String id;

    @JsonProperty("is_case_sensitive")
    @NotNull(groups = {ValidationGroup.CreateObjectValidationGroup.class}, message = "is_case_sensitive field is missing")
    private Boolean isCaseSensitive;

    @NotNull(groups = {ValidationGroup.CreateObjectValidationGroup.class}, message = "entries field is missing")
    private List<String> entries;

    @JsonProperty("is_deleted")
    private boolean isDeleted = false;
}
