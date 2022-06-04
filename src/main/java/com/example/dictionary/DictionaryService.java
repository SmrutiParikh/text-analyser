package com.example.dictionary;

import com.amirkhawaja.Ksuid;
import com.example.core.PositionDTO;
import com.example.exception.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;

@Service
@Slf4j(topic = "DICTIONARY_SERVICE")
public class DictionaryService {
    Map<String, Dictionary> dictionaries = new HashMap<>();

    public Dictionary create(Dictionary newRecord) throws IOException {
        newRecord.setId(new Ksuid().generate());
        return save(newRecord);
    }

    public Dictionary update(Dictionary updatedRecord) throws RecordNotFoundException {
        Dictionary oldRecord = findById(updatedRecord.getId());
        oldRecord.update(updatedRecord);
        return save(oldRecord);
    }

    public List<String> read(String id, PositionDTO position) throws RecordNotFoundException {
        Dictionary dictionary = findById(id);
        return dictionary.read(position.getStartIndex(), position.getEndIndex());
    }

    public Dictionary delete(String id) throws RecordNotFoundException {
        findById(id);
        return dictionaries.remove(id);
    }

    public Set<String> list() {
        return dictionaries.keySet();
    }

    public Dictionary findById(String id) throws RecordNotFoundException {
        if (!dictionaries.containsKey(id)) throw new RecordNotFoundException("Dictionary not found for id:" + id);
        return dictionaries.get(id);
    }

    private Dictionary save(Dictionary dictionary) {
        dictionaries.put(dictionary.getId(), dictionary);
        return dictionary;
    }
}
