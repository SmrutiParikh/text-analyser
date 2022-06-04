package com.example.controller;

import com.example.core.DictionaryDTO;
import com.example.dictionary.Dictionary;
import com.example.dictionary.DictionaryService;
import com.example.exception.RecordNotFoundException;
import com.example.validations.CreateObjectValidationGroup;
import com.example.validations.IdObjectValidationGroup;
import com.example.validations.RetrieveObjectValidationGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j(topic = "DICTIONARY_CONTROLLER")
@PropertySource("classpath:application.properties")
public class DictionaryController {

    @Autowired
    private DictionaryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/dictionary/create")
    public DictionaryDTO create(@RequestBody @Validated(CreateObjectValidationGroup.class) DictionaryDTO input) throws IOException {
        log.info("Create dictionary requested");
        Dictionary dictionary = service.create(convertToVO(input));
        log.info("Create dictionary processed for id: {}", dictionary.getId());
        return convertToDTO(dictionary);
    }

    @PutMapping("/dictionary/update")
    public DictionaryDTO update(@RequestBody @Validated(IdObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        log.info("Update dictionary requested for id : {}", input.getId());
        Dictionary dictionary = service.update(convertToVO(input));
        log.info("Update dictionary processed for id: {}", dictionary.getId());
        return convertToDTO(dictionary);
    }

    @PostMapping("/dictionary/read")
    public List<String> read(@RequestBody @Validated(RetrieveObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        return service.read(input.getId(), input.getPosition());
    }

    @DeleteMapping("/dictionary/delete")
    public DictionaryDTO delete(@RequestBody @Validated(IdObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        log.info("Delete dictionary requested for id : {}", input.getId());
        Dictionary dictionary = service.delete(input.getId());
        log.info("Delete dictionary processed for id : {}", input.getId());
        return convertToDTO(dictionary);
    }

    @GetMapping("/dictionary/list")
    public Set<String> list() {
        return service.list();
    }

    private DictionaryDTO convertToDTO(Dictionary vo) {
        return objectMapper.convertValue(vo, DictionaryDTO.class);
    }

    private Dictionary convertToVO(DictionaryDTO dto) {
        return objectMapper.convertValue(dto, Dictionary.class);
    }
}
