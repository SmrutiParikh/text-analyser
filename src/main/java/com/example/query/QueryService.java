package com.example.query;

import com.example.core.PositionDTO;
import com.example.dictionary.Dictionary;
import com.example.dictionary.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "QUERY_SERVICE")
public class QueryService {
    @Autowired
    private DictionaryService dictionaryService;

    public List<PositionDTO> analyseText(String dictionaryId, String target) throws Exception {
        Dictionary dictionary = dictionaryService.findById(dictionaryId);
        return dictionary.analyseText(target);
    }
}
