package org.example.lawcaseservice.controller.webclient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.controller.mapper.LawCaseMapper;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.service.LawCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/cases/webclient")
public class LawyerWebClientController {

    private final String DESCRIPTION_404_ID = "LawCase was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String DESCRIPTION_500_LONG = "Invalid input data or " + DESCRIPTION_500_SHORT;
    private final String LAWYER_NUMBER_QUERY_PATH = "/{lawyerId}";

    private final LawCaseService lawCaseService;

    public LawyerWebClientController ( LawCaseService lawCaseService ) {
        this.lawCaseService = lawCaseService;
    }

    @Operation(
            description = "Send LawCases to the Lawyer microservice by Lawyer Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCases delivered by Id and attached to the Lawyer"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_LONG)
    })
    @GetMapping("/sendLawCasesToLawyerService" + LAWYER_NUMBER_QUERY_PATH)
    @CircuitBreaker(name = "lawCaseServiceCB", fallbackMethod = "fallbackLawCases")
    @Retry(name = "lawCaseServiceRetry")
    public ResponseEntity<List<LawCaseResponseDto>> getLawCasesByLawyerId(
            @PathVariable UUID lawyerId) {
        List<LawCase> lawCasesToSend = lawCaseService.getAllLawCases()
                .stream()
                .filter(lawCase -> Objects.equals(lawCase.getLawClientId(), lawyerId))
                .toList();
        log.info("Sent {} LawCases to Lawyer with id: {}", lawCasesToSend.size(), lawyerId);
        List<LawCaseResponseDto> dtos = lawCasesToSend
                .stream()
                .map(LawCaseMapper::toDto)
                .toList();
        return ResponseEntity.ok((dtos));
    }

    // Fallback – jeśli serwis padnie, zwracamy pustą listę
    public ResponseEntity<List<LawCaseResponseDto>> fallbackLawCases(UUID lawyerId, Throwable t) {
        log.warn("Fallback triggered for lawyerId {} due to {}", lawyerId, t.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }
}
