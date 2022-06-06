package com.example.controller;

import com.example.core.dto.AnalyseQueryDTO;
import com.example.core.dto.DictionaryDTO;
import com.example.core.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QueryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DICTIONARIES_URL = "/dictionaries";
    private static final String BASE_URL = "/analyse";

    private byte[] toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(o);
    }

    @Test
    public void analyseTextCaseInsensitive() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(false)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult mvcResult = mockMvc.perform(
                            post(DICTIONARIES_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(false)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andReturn();

            DictionaryDTO created = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DictionaryDTO.class);
            AnalyseQueryDTO analyseQueryDTO = AnalyseQueryDTO.builder()
                    .dictionaryId(created.getId())
                    .target("Garden has a tree, several bushes and plants.")
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/text")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(analyseQueryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].start_index", is(13)))
                    .andExpect(jsonPath("$[0].end_index", is(16)))
                    .andExpect(jsonPath("$[1].start_index", is(27)))
                    .andExpect(jsonPath("$[1].end_index", is(30)))
                    .andExpect(jsonPath("$[2].start_index", is(38)))
                    .andExpect(jsonPath("$[2].end_index", is(42)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void analyseTextCaseSensitive() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(true)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult mvcResult = mockMvc.perform(
                            post(DICTIONARIES_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(true)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andReturn();

            DictionaryDTO created = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DictionaryDTO.class);
            AnalyseQueryDTO analyseQueryDTO = AnalyseQueryDTO.builder()
                    .dictionaryId(created.getId())
                    .target("Garden has a tree, several bushes and plants.")
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/text")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(analyseQueryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].start_index", is(13)))
                    .andExpect(jsonPath("$[0].end_index", is(16)))
                    .andExpect(jsonPath("$[1].start_index", is(38)))
                    .andExpect(jsonPath("$[1].end_index", is(42)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void analyseTextMissingDictionaryId() {
        try {
            AnalyseQueryDTO analyseQueryDTO = AnalyseQueryDTO.builder()
                    .target("Garden has a tree, several bushes and plants.")
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/text")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(analyseQueryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", Matchers.is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", Matchers.is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", Matchers.is("Invalid field(s): dictionaryId")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("dictionary_id field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void analyseTextMissingTarget() {
        try {
            AnalyseQueryDTO analyseQueryDTO = AnalyseQueryDTO.builder()
                    .dictionaryId("1")
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/text")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(analyseQueryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", Matchers.is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", Matchers.is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", Matchers.is("Invalid field(s): target")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("target field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void analyseTextInvalidDictionaryId() {
        try {
            AnalyseQueryDTO analyseQueryDTO = AnalyseQueryDTO.builder()
                    .dictionaryId("000")
                    .target("Garden has a tree, several bushes and plants.")
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/text")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(analyseQueryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.http_code", Matchers.is(HttpStatus.NOT_FOUND.value())))
                    .andExpect(jsonPath("$.error_code", Matchers.is(ErrorCode.NOT_FOUND.name())))
                    .andExpect(jsonPath("$.error_message", Matchers.is("Dictionary not found for id: 000")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
