package org.example.lawcaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawcaseservice.controller.webclient.LawyerWebClientController;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.service.LawCaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LawyerWebClientController.class)
class LawyerWebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawCaseService lawCaseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getLawCasesByLawyerId_ShouldReturn200() throws Exception {
        UUID lawyerId = UUID.randomUUID();

        LawCase case1 = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawClientId(lawyerId)
                .build();

        LawCase case2 = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case B")
                .lawClientId(UUID.randomUUID())
                .build();

        when(lawCaseService.getAllLawCases()).thenReturn(List.of(case1, case2));

        mockMvc.perform(
                        get("/api/cases/webclient/sendLawCasesToLawyerService/{lawyerId}", lawyerId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Case A"))
                .andExpect(jsonPath("$[0].id").value(case1.getId().toString()));

        verify(lawCaseService).getAllLawCases();
    }

    @Test
    void getLawCasesByLawyerId_ShouldReturnEmptyList() throws Exception {
        UUID lawyerId = UUID.randomUUID();

        when(lawCaseService.getAllLawCases()).thenReturn(List.of());

        mockMvc.perform(
                        get("/api/cases/webclient/sendLawCasesToLawyerService/{lawyerId}", lawyerId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(lawCaseService).getAllLawCases();
    }

    @Test
    void getLawCasesByLawyerId_ShouldReturn500_OnServiceException() throws Exception {
        UUID lawyerId = UUID.randomUUID();

        when(lawCaseService.getAllLawCases()).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(
                        get("/api/cases/webclient/sendLawCasesToLawyerService/{lawyerId}", lawyerId)
                )
                .andExpect(status().is5xxServerError());

        verify(lawCaseService).getAllLawCases();
    }
}