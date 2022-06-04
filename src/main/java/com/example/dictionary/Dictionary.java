package com.example.dictionary;

import com.example.core.PositionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

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

    public List<String> read(int start, int end) {
        return null;
    }

    public List<PositionDTO> analyseText(String target) {
        return null;
    }
}
