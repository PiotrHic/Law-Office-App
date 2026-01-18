package org.example.lawcaseservice.controller;

import org.example.lawcaseservice.controller.webclient.LawClientWebClientController;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.service.LawCaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LawClientWebClientController.class)
class LawClientWebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawCaseService lawCaseService;

    private LawCase case1;
    private LawCase case2;

    @BeforeEach
    void setUp() {
        case1 = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case 1")
                .lawClientId(UUID.randomUUID())
                .build();

        case2 = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case 2")
                .lawClientId(UUID.randomUUID())
                .build();
    }

    //@Test
    void getLawCasesByLawyerId_ShouldReturnFilteredList() throws Exception {
        // given: service zwraca 2 przypadki
        when(lawCaseService.getAllLawCases()).thenReturn(List.of(case1, case2));

        // when: żądamy case'ów o konkretnym lawClientId = 100
        mockMvc.perform(get("/api/cases/webclient/sendLawCasesToLawCLientService/{lawClientId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Case 1"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    //@Test
    void getLawCasesByLawyerId_ShouldReturnEmptyList_WhenNoMatches() throws Exception {
        when(lawCaseService.getAllLawCases()).thenReturn(List.of(case1, case2));

        mockMvc.perform(get("/api/cases/webclient/sendLawCasesToLawCLientService/{lawClientId}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    //@Test
    void getLawCasesByLawyerId_ShouldReturn200_WhenListIsEmptyInService() throws Exception {
        when(lawCaseService.getAllLawCases()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cases/webclient/sendLawCasesToLawCLientService/{lawClientId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}