package com.example.controller;

import com.example.core.ValidationGroup;
import com.example.core.dto.DictionaryDTO;
import com.example.core.exception.RecordNotFoundException;
import com.example.dictionary.Dictionary;
import com.example.dictionary.DictionaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dictionaries")
@Slf4j(topic = "DICTIONARY_CONTROLLER")
@PropertySource("classpath:application.properties")
public class DictionaryController {

    @Autowired
    private DictionaryService service;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param input (DictionaryDTO)
     *              requires a pojo for new dictionary
     * @return (DictionaryDTO)
     * dictionary pojo with id
     */
    @PostMapping("/create")
    public DictionaryDTO create(@RequestBody @Validated(ValidationGroup.CreateObjectValidationGroup.class) DictionaryDTO input) {
        log.info("Create dictionary requested");
        Dictionary dictionary = service.create(convertToVO(input));
        log.info("Create dictionary processed for id: {}", dictionary.getId());
        return convertToDTO(dictionary);
    }

    /**
     * @param input (DictionaryDTO)
     *              requires a pojo for updating dictionary
     * @return (DictionaryDTO)
     * updated dictionary pojo
     * @throws RecordNotFoundException when unable to find mentioned record
     */
    @PutMapping("/update")
    public DictionaryDTO update(@RequestBody @Validated(ValidationGroup.IdObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        log.info("Update dictionary requested for id : {}", input.getId());
        Dictionary dictionary = service.update(convertToVO(input));
        log.info("Update dictionary processed for id: {}", dictionary.getId());
        return convertToDTO(dictionary);
    }

    /**
     * @param input (DictionaryDTO)
     *              required id of dictionary
     * @return (List < String >)
     * list of entries in dictionary
     * @throws RecordNotFoundException when unable to find mentioned record
     */
    @PostMapping(value = "/read")
    public List<String> read(@RequestBody @Validated(ValidationGroup.IdObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        return service.read(input.getId());
    }

    /**
     * @param input (DictionaryDTO)
     *              required id of dictionary
     * @return (DictionaryDTO)
     * deleted object
     * @throws RecordNotFoundException when unable to find mentioned record
     */
    @DeleteMapping("/delete")
    public DictionaryDTO delete(@RequestBody @Validated(ValidationGroup.IdObjectValidationGroup.class) DictionaryDTO input) throws RecordNotFoundException {
        log.info("Delete dictionary requested for id : {}", input.getId());
        DictionaryDTO dictionaryDTO = convertToDTO(service.delete(input.getId()));
        dictionaryDTO.setDeleted(true);
        log.info("Delete dictionary processed for id : {}", input.getId());
        return dictionaryDTO;
    }

    /**
     * @return (Set < String >)
     * lists ids of all dictionaries
     */
    @GetMapping("/list")
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
