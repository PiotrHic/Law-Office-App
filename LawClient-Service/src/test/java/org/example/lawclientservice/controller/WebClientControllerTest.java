package org.example.lawclientservice.controller;

import org.example.lawclientservice.client.dto.LawCaseDto;
import org.example.lawclientservice.client.dto.LawClientResponseDto;
import org.example.lawclientservice.client.webclient.LawCaseWebClient;
import org.example.lawclientservice.domain.LawCase;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.service.LawClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebClientController.class)
class WebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawClientService lawClientService;

    @MockBean
    private LawCaseWebClient lawCaseWebClient;

    private UUID clientId;
    private LawClient lawClient;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        lawClient = new LawClient(clientId, "John Doe");
    }

    @Test
    void shouldReturnLawCasesForClient() throws Exception {
        // Mock klienta
        when(lawClientService.getLawClientByID(clientId))
                .thenReturn(Optional.of(lawClient));

        // Mock WebClient – zwraca listę spraw
        when(lawCaseWebClient.getLawCasesByLawClientId(clientId))
                .thenReturn(List.of(
                        new LawCaseDto(UUID.randomUUID(), "Test Case", clientId)
                ));

        // Wykonanie GET i weryfikacja JSON
        mockMvc.perform(get("/api/clients/webclient/getLawCases/{lawClientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.lawCases[0].name").value("Test Case"));
    }

    @Test
    void fallbackShouldReturnEmptyList() {
        // Mock klienta
        when(lawClientService.getLawClientByID(clientId)).thenReturn(Optional.of(lawClient));

        WebClientController controller = new WebClientController(lawCaseWebClient, lawClientService);

        // Wywołanie fallbacka ręcznie
        ResponseEntity<LawClientResponseDto> response =
                controller.fallbackGetLawCases(clientId, new RuntimeException("Service down"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
        assertThat(response.getBody().getLawCases()).isEmpty(); // <- fallback zwraca pustą listę
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {
        when(lawClientService.getLawClientByID(clientId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/webclient/getLawCases/{lawClientId}", clientId))
                .andExpect(status().isNotFound());
    }
}