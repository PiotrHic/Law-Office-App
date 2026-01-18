package org.example.lawcaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawcaseservice.client.dto.LawCaseRequestDto;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.domain.LawClient;
import org.example.lawcaseservice.domain.Lawyer;
import org.example.lawcaseservice.exception.GlobalExceptionHandler;
import org.example.lawcaseservice.service.LawCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LawCaseController.class)
@Import(GlobalExceptionHandler.class)
class LawCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawCaseService lawCaseService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private LawCase sampleCase;

    @BeforeEach
    void setUp() {
        sampleCase = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .lawyer(new Lawyer(UUID.randomUUID(),"John Lawyer 1"))
                .lawClient(new LawClient (UUID.randomUUID(),"John Client 1"))
                .lawClientId(UUID.randomUUID())
                .build();
    }

    // CREATE
    @Test
    void createLawCase_ShouldReturn201() throws Exception {
        LawCaseRequestDto request = LawCaseRequestDto.builder()
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .lawClientId(UUID.randomUUID())
                .build();

        when(lawCaseService.createLawCase(any())).thenReturn(Optional.of(sampleCase));

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Case A"));
    }

    // GET BY ID
    @Test
    void getLawCaseById_ShouldReturn200() throws Exception {
        when(lawCaseService.getLawCaseById(sampleCase.getId())).thenReturn(Optional.of(sampleCase));

        mockMvc.perform(get("/api/cases/by-id/{id}", sampleCase.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCase.getId().toString()))
                .andExpect(jsonPath("$.name").value("Case A"));
    }

    @Test
    void getLawCaseById_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(lawCaseService.getLawCaseById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cases/by-id/{id}", id))
                .andExpect(status().isNotFound());
    }

    // GET BY NAME
    @Test
    void getLawCasesByName_ShouldReturn200() throws Exception {
        when(lawCaseService.getLawCaseByName("Case"))
                .thenReturn(List.of(sampleCase));

        mockMvc.perform(get("/api/cases/by-name/{name}", "Case"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Case A"));
    }

    // GET ALL
    @Test
    void getAllLawCases_ShouldReturn200() throws Exception {
        when(lawCaseService.getAllLawCases()).thenReturn(List.of(sampleCase));

        mockMvc.perform(get("/api/cases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleCase.getId().toString()))
                .andExpect(jsonPath("$[0].name").value("Case A"));
    }

    // UPDATE
    @Test
    void updateLawCase_ShouldReturn200() throws Exception {
        LawCaseRequestDto request = LawCaseRequestDto.builder()
                .name("Case Updated")
                .lawyerId(UUID.randomUUID())
                .lawClientId(UUID.randomUUID())
                .build();

        LawCase updated = LawCase.builder()
                .id(sampleCase.getId())
                .name("Case Updated")
                .lawyerId(UUID.randomUUID())
                .lawClientId(UUID.randomUUID())
                .build();

        when(lawCaseService.updateLawCaseById(eq(sampleCase.getId()), any()))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/cases/{id}", sampleCase.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Case Updated"));
    }

    @Test
    void updateLawCase_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();

        LawCaseRequestDto request = LawCaseRequestDto.builder()
                .name("Not Exists")
                .lawyerId(UUID.randomUUID())
                .lawClientId(UUID.randomUUID())
                .build();

        when(lawCaseService.updateLawCaseById(eq(id), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cases/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // DELETE ONE
    @Test
    void deleteLawCase_ShouldReturn204() throws Exception {
        when(lawCaseService.deleteLawCaseById(sampleCase.getId()))
                .thenReturn(Optional.of(sampleCase));

        mockMvc.perform(delete("/api/cases/{id}", sampleCase.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLawCase_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(lawCaseService.deleteLawCaseById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/cases/{id}", id))
                .andExpect(status().isNotFound());
    }

    // DELETE ALL
    @Test
    void deleteAllLawCases_ShouldReturn200() throws Exception {
        when(lawCaseService.deleteAllLawCases()).thenReturn("All deleted");

        mockMvc.perform(delete("/api/cases"))
                .andExpect(status().isOk())
                .andExpect(content().string("All deleted"));
    }
}