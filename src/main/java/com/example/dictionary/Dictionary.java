package com.example.dictionary;

import com.example.core.dto.PositionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Dictionary {
    private String id;

    @JsonProperty("is_case_sensitive")
    private Boolean isCaseSensitive;

    private List<String> entries;

    public void setId(String id) {
        this.id = id;
    }

    public void update(Dictionary input) {
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
        return matcher.find() ? new PositionDTO(matcher.start(), matcher.end() - 1) : null;
    }
}
