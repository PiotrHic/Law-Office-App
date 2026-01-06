package org.example.lawyerservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.lawyerservice.client.dto.LawyerResponseDto;
import org.example.lawyerservice.client.webclient.LawCaseWebClient;
import org.example.lawyerservice.controller.mapper.LawCaseMapper;
import org.example.lawyerservice.controller.mapper.LawyerMapper;
import org.example.lawyerservice.domain.LawCase;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.exception.LawyerNotFoundException;
import org.example.lawyerservice.service.LawyerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/lawyers/webclient")
public class WebClientController {

    private final String DESCRIPTION_404_ID = "Lawyer was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String NUMBER_QUERY_PATH = "/{lawyerId}";

    private final LawCaseWebClient lawCaseWebClient;
    private final LawyerService lawyerService;

    public WebClientController (LawyerService lawyerService, LawCaseWebClient lawCaseWebClient) {
        this.lawCaseWebClient = lawCaseWebClient;
        this.lawyerService = lawyerService;
    }


    @Operation(
            description = "Send Request to the LawCase Service to bring the list " +
                    "of LawCases to the Lawyer by the Lawyer id - /api/lawyer/webclient/getLawCases/1"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCases delivered by Lawyer Id " +
                    "and attached to the Lawyer"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/getLawCases" + NUMBER_QUERY_PATH)
    ResponseEntity<LawyerResponseDto> getLawCaseByLawyerId(@PathVariable String lawyerId){

        log.info("GET /lawyers/{}", lawyerId);

        Lawyer founded = lawyerService.getLawyerByID(lawyerId).orElseThrow(()
                -> new LawyerNotFoundException ("Lawyer not found with id=" + lawyerId));

        List<LawCase> lawCases = new ArrayList<>();
        try {
            lawCases = lawCaseWebClient.getLawCasesByLawyerId(lawyerId)
                    .stream()
                    .map(LawCaseMapper::toEntity)
                    .toList();
        } catch (WebClientResponseException e) {
            log.error("CASES service error: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException (
                    HttpStatus.BAD_GATEWAY,
                    "LawCase Service unavailable"
            );
        }

        founded.setLawCaseList(lawCases);
        lawyerService.updateLawyerById(lawyerId, founded);
        log.info("LawCases were attached to Lawyer by the lawyerID : {}!", lawyerId);
        return ResponseEntity.ok(LawyerMapper.toDto(founded));
    }
}
