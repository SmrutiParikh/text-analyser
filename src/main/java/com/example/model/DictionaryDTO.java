package com.example.model;

import com.example.utils.ValidationGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Builder.Default
    @JsonProperty("is_deleted")
    private boolean isDeleted = false;

    public void setId(String id) {
        this.id = id;
    }

    public void update(DictionaryDTO input) {
        if (Objects.nonNull(input.getIsCaseSensitive())) {
            this.isCaseSensitive = input.getIsCaseSensitive();
        }

        if (!CollectionUtils.isEmpty(input.getEntries())) {
            this.entries = input.getEntries();
        }
    }

    public List<PositionDTO> analyseText(String target) {
        return Optional.ofNullable(this.entries)
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> getPosition(target, e))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // returns start and end indices of first match
    private PositionDTO getPosition(String target, String entry) {
        Pattern pattern = Pattern.compile(entry, this.isCaseSensitive ? 0: Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find() ? new PositionDTO(matcher.group(), matcher.start(), matcher.end() - 1) : null;
    }
}
