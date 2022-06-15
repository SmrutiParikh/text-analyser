package com.example.controller;

import com.example.model.DictionaryDTO;
import com.example.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DictionaryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/dictionaries";

    private byte[] toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(o);
    }

    @Test
    public void createCaseInsensitive() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(false)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult result = mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(false)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andExpect(jsonPath("$.is_deleted", is(false)))
                    .andReturn();

            DictionaryDTO dictionaryDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DictionaryDTO.class);

            mockMvc.perform(
                            delete(BASE_URL + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dictionaryDTO.getId())))
                    .andExpect(jsonPath("$.is_deleted", is(true)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createCaseSensitive() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(true)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult result = mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(true)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andExpect(jsonPath("$.is_deleted", is(false)))
                    .andReturn();

            DictionaryDTO dictionaryDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DictionaryDTO.class);

            mockMvc.perform(
                            delete(BASE_URL + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dictionaryDTO.getId())))
                    .andExpect(jsonPath("$.is_deleted", is(true)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createBadRequestFlagIsMissing() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", is("Invalid field(s): isCaseSensitive")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("is_case_sensitive field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createBadRequestEntriesAreMissing() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(false)
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", is("Invalid field(s): entries")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("entries field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void update() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(false)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult result = mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(false)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andExpect(jsonPath("$.is_deleted", is(false)))
                    .andReturn();

            DictionaryDTO dictionaryDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DictionaryDTO.class);
            dictionaryDTO.getEntries().add("Flower");

            mockMvc.perform(
                            put(BASE_URL + "/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dictionaryDTO.getId())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(false)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(4)))
                    .andExpect(jsonPath("$.entries[3]", is("Flower")))
                    .andExpect(jsonPath("$.is_deleted", is(false)));

            mockMvc.perform(
                            delete(BASE_URL + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dictionaryDTO.getId())))
                    .andExpect(jsonPath("$.is_deleted", is(true)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void updateBadRequestIdIsMissing() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .build();

            mockMvc.perform(
                            put(BASE_URL + "/update")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", is("Invalid field(s): id")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("id field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void read() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(false)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult result = mockMvc.perform(
                            post(BASE_URL + "/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(notNullValue())))
                    .andExpect(jsonPath("$.is_case_sensitive", is(false)))
                    .andExpect(jsonPath("$.entries", is(notNullValue())))
                    .andExpect(jsonPath("$.entries", hasSize(3)))
                    .andExpect(jsonPath("$.entries[1]", is("BuSh")))
                    .andExpect(jsonPath("$.is_deleted", is(false)))
                    .andReturn();

            DictionaryDTO dictionaryDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DictionaryDTO.class);

            mockMvc.perform(
                            post(BASE_URL + "/read")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0]", is("tree")))
                    .andExpect(jsonPath("$[1]", is("BuSh")))
                    .andExpect(jsonPath("$[2]", is("plant")));

            mockMvc.perform(
                            delete(BASE_URL + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(dictionaryDTO))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dictionaryDTO.getId())))
                    .andExpect(jsonPath("$.is_deleted", is(true)));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void readBadRequestIdIsMissing() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .build();

            mockMvc.perform(
                            post(BASE_URL + "/read")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", is("Invalid field(s): id")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("id field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void deleteBadRequestIdIsMissing() {

        try {
            DictionaryDTO input = DictionaryDTO.builder()
                    .build();

            mockMvc.perform(
                            delete(BASE_URL + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(input))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.http_code", is(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.error_code", is(ErrorCode.BAD_REQUEST.name())))
                    .andExpect(jsonPath("$.error_message", is("Invalid field(s): id")))
                    .andExpect(jsonPath("$.subErrors[0].message", is("id field is missing")));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void list() {

        try {

            DictionaryDTO input = DictionaryDTO.builder()
                    .isCaseSensitive(true)
                    .entries(List.of("tree", "BuSh", "plant"))
                    .build();

            MvcResult result = mockMvc.perform(
                            post(BASE_URL + "/create")
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

            DictionaryDTO dictionaryDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DictionaryDTO.class);

            mockMvc.perform(
                            get(BASE_URL + "/list")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0]", is(dictionaryDTO.getId())));

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

}
