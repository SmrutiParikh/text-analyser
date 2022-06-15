package com.example.controller;

import com.example.utils.ValidationGroup;
import com.example.model.DictionaryDTO;
import com.example.exception.RecordNotFoundException;
import com.example.service.DictionaryService;
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
        DictionaryDTO dictionary = service.create(input);
        log.info("Create dictionary processed for id: {}", dictionary.getId());
        return dictionary;
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
        DictionaryDTO dictionary = service.update(input);
        log.info("Update dictionary processed for id: {}", dictionary.getId());
        return dictionary;
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
        DictionaryDTO dictionaryDTO = service.delete(input.getId());
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
}
