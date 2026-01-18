package org.example.lawclientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.service.LawClientService;
import org.example.lawyerservice.client.dto.LawCaseDto;
import org.example.lawyerservice.client.webclient.LawCaseWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(WebClientController.class)
class WebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawClientService lawClientService;

    @MockBean
    private LawCaseWebClient lawCaseWebClient;

    @Autowired
    private ObjectMapper objectMapper;

    // @Test
    void getLawCasesByLawClientId_ShouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        LawClient client = LawClient.builder()
                .id(id)
                .name("Adam Nowak")
                .lawCases(List.of())
                .build();

        when(lawClientService.getLawClientByID(id)).thenReturn(Optional.of(client));

        LawCaseDto caseDto = LawCaseDto.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawClientId(id)
                .build();

        when(lawCaseWebClient.getLawCasesByLawyerId(id)).thenReturn(List.of(caseDto));
        when(lawClientService.updateLawClientById(eq(id), any())).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/webclient/getLawCases/{lawClientId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Adam Nowak"))
                .andExpect(jsonPath("$.lawCases[0].name").value("Case A"));

        verify(lawClientService).getLawClientByID(id);
        verify(lawCaseWebClient).getLawCasesByLawyerId(id);
        verify(lawClientService).updateLawClientById(eq(id), any());
    }

    // @Test
    void getLawCasesByLawClientId_ShouldReturn404_WhenClientNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(lawClientService.getLawClientByID(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/webclient/getLawCases/{lawClientId}", id))
                .andExpect(status().isNotFound());

        verify(lawClientService).getLawClientByID(id);
        verifyNoInteractions(lawCaseWebClient);
    }

    // @Test
    void getLawCasesByLawClientId_ShouldReturn502_WhenWebClientFails() throws Exception {
        UUID id = UUID.randomUUID();

        LawClient client = LawClient.builder()
                .id(id)
                .name("Adam Nowak")
                .lawCases(List.of())
                .build();

        when(lawClientService.getLawClientByID(id)).thenReturn(Optional.of(client));

        WebClientResponseException exception = WebClientResponseException.create(
                500,
                "Internal Error",
                null,
                "Service down".getBytes(),
                StandardCharsets.UTF_8
        );

        doThrow(exception).when(lawCaseWebClient).getLawCasesByLawyerId(id);

        mockMvc.perform(get("/api/clients/webclient/getLawCases/{lawClientId}", id))
                .andExpect(status().isBadGateway());

        verify(lawClientService).getLawClientByID(id);
        verify(lawCaseWebClient).getLawCasesByLawyerId(id);
    }
}