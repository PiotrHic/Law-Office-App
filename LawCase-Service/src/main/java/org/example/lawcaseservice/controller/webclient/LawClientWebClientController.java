package org.example.lawcaseservice.controller.webclient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.controller.LawCaseMapper;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.service.LawCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/api/cases/webclient")
public class LawClientWebClientController {

    private final String DESCRIPTION_404_ID = "LawCase was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String DESCRIPTION_500_LONG = "Invalid input data or " + DESCRIPTION_500_SHORT;
    private final String LAWYER_NUMBER_QUERY_PATH = "/{lawClientId}";
    private final LawCaseService lawCaseService;

    public LawClientWebClientController(LawCaseService lawCaseService) {
        this.lawCaseService = lawCaseService;
    }

    @Operation(
            description = "Send LawCases to the LawClient microservice by LawClient Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCases delivered by Id and attached to the LawClient"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_LONG)
    })
    @GetMapping("/sendLawCasesToLawCLientService" + LAWYER_NUMBER_QUERY_PATH)
    public ResponseEntity<List<LawCaseResponseDto>> getLawCasesByLawyerId(
            @PathVariable String lawClientId) {
        List<LawCase> lawCasesToSend = lawCaseService.getAllLawCases()
                .stream()
                .filter(lawCase -> Objects.equals(lawClientId, lawCase.getLawClientId()))
                .toList();
        log.info("List of LawCases were send to the LawClient with id: {}!", lawClientId);
        List<LawCaseResponseDto> dtos = lawCasesToSend
                .stream()
                .map(LawCaseMapper::toDto)
                .toList();
        return ResponseEntity.ok((dtos));
    }
}
