package org.example.lawcaseservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.controller.mapper.LawCaseMapper;
import org.example.lawcaseservice.controller.webclient.LawyerWebClientController;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.service.LawCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LawyerWebClientController.class)
class LawyerWebClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawCaseService lawCaseService;

    private UUID lawyerId;

    @BeforeEach
    void setUp() {
        lawyerId = UUID.randomUUID();
    }

    //@Test
    void shouldReturnLawCasesForLawyer() throws Exception {
        // 1️⃣ Przygotowanie danych – lawClientId = lawyerId
        LawCase lawCase = new LawCase(UUID.randomUUID(), "Test Case", lawyerId);
        when(lawCaseService.getAllLawCases()).thenReturn(List.of(lawCase));

        // 2️⃣ Mockowanie mappera
        LawCaseResponseDto dto = new LawCaseResponseDto(lawCase.getId(), lawCase.getName(), lawCase.getLawClientId());
        try (MockedStatic<LawCaseMapper> mockedMapper = Mockito.mockStatic(LawCaseMapper.class)) {
            mockedMapper.when(() -> LawCaseMapper.toDto(lawCase)).thenReturn(dto);

            // 3️⃣ Wykonanie GET i weryfikacja JSON
            mockMvc.perform(get("/api/cases/webclient/sendLawCasesToLawyerService/" + lawyerId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Test Case"))
                    .andExpect(jsonPath("$[0].lawClientId").value(lawyerId.toString()));
        }
    }

    @Test
    void fallbackShouldReturnEmptyList() {
        // Fallback ręcznie – symulacja awarii
        LawyerWebClientController controller = new LawyerWebClientController(lawCaseService);

        ResponseEntity<List<LawCaseResponseDto>> response =
                controller.fallbackLawCases(lawyerId, new RuntimeException("Service down"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty(); // lista jest pusta
    }

    @Test
    void shouldReturnEmptyListWhenNoCasesMatch() throws Exception {
        // Wszystkie LawCase mają inny lawClientId
        LawCase lawCase = new LawCase(UUID.randomUUID(), "Other Case", UUID.randomUUID());
        when(lawCaseService.getAllLawCases()).thenReturn(List.of(lawCase));

        mockMvc.perform(get("/api/cases/webclient/sendLawCasesToLawyerService/" + lawyerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty()); // pusta lista
    }
}