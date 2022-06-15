package com.example.controller;

import com.example.model.AnalyseQueryDTO;
import com.example.model.PositionDTO;
import com.example.exception.RecordNotFoundException;
import com.example.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j(topic = "QUERY_CONTROLLER")
@PropertySource("classpath:application.properties")
public class QueryController {

    @Autowired
    private QueryService textAnalyseService;

    /**
     * @param input (AnalyseQueryDTO)
     *              requires dictionary id and a target string
     * @return List<PositionDTO>
     * list of positions of start and end indices match of entries with target
     * @throws RecordNotFoundException when unable to find mentioned record
     */
    @PostMapping("/analyse/text")
    public List<PositionDTO> analyseText(@RequestBody @Validated AnalyseQueryDTO input) throws RecordNotFoundException {
        log.info("Analyse text requested for dictionary id: {}, target: {}", input.getDictionaryId(), input.getTarget());
        List<PositionDTO> result = textAnalyseService.analyseText(input.getDictionaryId(), input.getTarget());
        log.info("Analyse text processed for dictionary id: {}, target: {}", input.getDictionaryId(), input.getTarget());
        return result;
    }
}

