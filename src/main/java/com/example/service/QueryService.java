package com.example.service;

import com.example.model.DictionaryDTO;
import com.example.model.PositionDTO;
import com.example.exception.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "QUERY_SERVICE")
public class QueryService {
    @Autowired
    private DictionaryService dictionaryService;

    public List<PositionDTO> analyseText(String dictionaryId, String target) throws RecordNotFoundException {
        DictionaryDTO dictionary = dictionaryService.findById(dictionaryId);
        return dictionary.analyseText(target);
    }
}
