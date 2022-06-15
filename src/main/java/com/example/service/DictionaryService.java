package com.example.service;

import com.amirkhawaja.Ksuid;
import com.example.exception.RecordNotFoundException;
import com.example.model.DictionaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;

@Service
@Slf4j(topic = "DICTIONARY_SERVICE")
public class DictionaryService {
    Map<String, DictionaryDTO> dictionaries = new HashMap<>();

    public DictionaryDTO create(DictionaryDTO newRecord) {
        try {
            newRecord.setId(new Ksuid().generate());
        } catch (IOException e) {
            newRecord.setId(String.valueOf(new Random().nextInt()));
        }
        return save(newRecord);
    }

    public DictionaryDTO update(DictionaryDTO updatedRecord) throws RecordNotFoundException {
        DictionaryDTO oldRecord = findById(updatedRecord.getId());
        oldRecord.update(updatedRecord);
        return save(oldRecord);
    }

    public List<String> read(String id) throws RecordNotFoundException {
        DictionaryDTO dictionary = findById(id);
        return dictionary.getEntries();
    }

    public DictionaryDTO delete(String id) throws RecordNotFoundException {
        findById(id);
        return dictionaries.remove(id);
    }

    public Set<String> list() {
        return dictionaries.keySet();
    }

    public DictionaryDTO findById(String id) throws RecordNotFoundException {
        if (!dictionaries.containsKey(id)) throw new RecordNotFoundException("Dictionary not found for id: " + id);
        return dictionaries.get(id);
    }

    private DictionaryDTO save(DictionaryDTO dictionary) {
        dictionaries.put(dictionary.getId(), dictionary);
        return dictionary;
    }
}
