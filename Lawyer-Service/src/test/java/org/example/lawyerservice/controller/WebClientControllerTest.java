package org.example.lawyerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawyerservice.client.dto.LawCaseDto;
import org.example.lawyerservice.client.webclient.LawCaseWebClient;
import org.example.lawyerservice.domain.Lawyer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.lawyerservice.service.LawyerService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WebClientController.class)
class WebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawyerService lawyerService;

    @MockBean
    private LawCaseWebClient lawCaseWebClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getLawCasesByLawyerId_ShouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        // mock lawyer
        Lawyer lawyer = Lawyer.builder()
                .id(id)
                .name("John Smith")
                .lawCaseList(List.of())
                .build();

        when(lawyerService.getLawyerById(id)).thenReturn(Optional.of(lawyer));

        // mock webclient cases
        LawCaseDto dto = LawCaseDto.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(id)
                .build();

        when(lawCaseWebClient.getLawCasesByLawyerId(id))
                .thenReturn(List.of(dto));

        when(lawyerService.updateLawyerById(eq(id), any()))
                .thenReturn(Optional.of(lawyer));

        mockMvc.perform(get("/api/lawyers/webclient/getLawCases/{lawyerId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.lawCases[0].name").value("Case A"));

        verify(lawyerService).getLawyerById(id);
        verify(lawCaseWebClient).getLawCasesByLawyerId(id);
        verify(lawyerService).updateLawyerById(eq(id), any());
    }

    @Test
    void getLawCasesByLawyerId_ShouldReturn404_WhenLawyerNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(lawyerService.getLawyerById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lawyers/webclient/getLawCases/{lawyerId}", id))
                .andExpect(status().isNotFound());

        verify(lawyerService).getLawyerById(id);
        verifyNoInteractions(lawCaseWebClient);
    }

    @Test
    void getLawCasesByLawyerId_ShouldReturn500_WhenWebClientFails() throws Exception {
        UUID id = UUID.randomUUID();

        Lawyer lawyer = Lawyer.builder()
                .id(id)
                .name("John Smith")
                .lawCaseList(List.of())
                .build();

        when(lawyerService.getLawyerById(id)).thenReturn(Optional.of(lawyer));

        WebClientResponseException exception = WebClientResponseException.create(
                500,
                "Internal Error",
                null,
                "Service down".getBytes(),
                StandardCharsets.UTF_8
        );

        doThrow(exception).when(lawCaseWebClient).getLawCasesByLawyerId(id);

        mockMvc.perform(get("/api/lawyers/webclient/getLawCases/{lawyerId}", id))
                .andExpect(status().is5xxServerError()); // 502

        verify(lawyerService).getLawyerById(id);
        verify(lawCaseWebClient).getLawCasesByLawyerId(id);
    }
}