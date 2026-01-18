package org.example.lawyerservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.example.lawyerservice.client.dto.LawCaseDto;
import org.example.lawyerservice.client.dto.LawyerResponseDto;
import org.example.lawyerservice.client.webclient.LawCaseWebClient;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.service.LawyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebClientController.class)
class WebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawyerService lawyerService;

    @MockBean
    private LawCaseWebClient lawCaseWebClient;

    private UUID lawyerId;
    private Lawyer lawyer;

    @BeforeEach
    void setUp() {
        lawyerId = UUID.randomUUID();
        lawyer = new Lawyer(lawyerId, "John Doe");
    }

    @Test
    void shouldReturnLawCasesForLawyer() throws Exception {
        UUID lawyerId = UUID.randomUUID();

        // Mock prawnika
        Lawyer lawyer = new Lawyer(lawyerId, "John Doe");
        when(lawyerService.getLawyerById(lawyerId)).thenReturn(java.util.Optional.of(lawyer));

        // Mock WebClient – zwraca List<LawCaseDto>
        when(lawCaseWebClient.getLawCasesByLawyerId(lawyerId))
                .thenReturn(List.of(
                        new LawCaseDto(UUID.randomUUID(), "Test Case", lawyerId)
                ));

        // Wykonanie GET i weryfikacja
        mockMvc.perform(get("/api/lawyers/webclient/getLawCases/{lawyerId}", lawyerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.lawCases[0].name").value("Test Case")); // <- tu poprawione
    }

    @Test
    void fallbackShouldReturnEmptyList() {
        UUID lawyerId = UUID.randomUUID();
        Lawyer lawyer = new Lawyer(lawyerId, "John Doe");

        // Mock LawyerService
        when(lawyerService.getLawyerById(lawyerId)).thenReturn(Optional.of(lawyer));

        WebClientController controller = new WebClientController(lawyerService, lawCaseWebClient);

        // Wywołanie fallbacka ręcznie
        ResponseEntity<LawyerResponseDto> response = controller.fallbackGetLawCases(lawyerId, new RuntimeException("Service down"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
        assertThat(response.getBody().getLawCases()).isEmpty(); // <- pusta lista
    }

    @Test
    void shouldReturn404WhenLawyerNotFound() throws Exception {
        when(lawyerService.getLawyerById(lawyerId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lawyers/webclient/getLawCases/{lawyerId}", lawyerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}