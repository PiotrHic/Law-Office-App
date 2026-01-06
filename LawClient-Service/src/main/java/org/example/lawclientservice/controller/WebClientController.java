package org.example.lawclientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.lawclientservice.client.dto.LawClientResponseDto;
import org.example.lawclientservice.client.webclient.LawCaseWebClient;
import org.example.lawclientservice.controller.mapper.LawCaseMapper;
import org.example.lawclientservice.controller.mapper.LawClientMapper;
import org.example.lawclientservice.domain.LawCase;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.exception.LawClientNotFoundException;
import org.example.lawclientservice.service.LawClientService;
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
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/clients/webclient")
public class WebClientController {

    private final String DESCRIPTION_404_ID = "LawClient was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String NUMBER_QUERY_PATH = "/{lawClientId}";

    private final LawCaseWebClient lawCaseWebClient;
    private final LawClientService lawClientService;

    public WebClientController ( LawCaseWebClient lawCaseWebClient, LawClientService lawClientService ) {
        this.lawCaseWebClient = lawCaseWebClient;
        this.lawClientService = lawClientService;
    }


    @Operation(
            description = "Send Request to the LawCase Service to bring the list " +
                    "of LawCases to the LawClient by the LawClient id - /api/lawyer/webclient/getLawCases/1"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCases delivered by LawClient Id " +
                    "and attached to the LawClient"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/getLawCases" + NUMBER_QUERY_PATH)
    ResponseEntity<LawClientResponseDto> getLawCaseByLawClientId(@PathVariable UUID lawClientId){

        log.info("GET /client/{}", lawClientId);

        LawClient founded = lawClientService.getLawClientByID(lawClientId).orElseThrow(()
                -> new LawClientNotFoundException("LawClient not found with id=" + lawClientId));

        List<LawCase> lawCases;
        try {
            lawCases = lawCaseWebClient.getLawCasesByLawClientId(lawClientId)
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

        founded.setLawCases(lawCases);
        lawClientService.updateLawClientById(lawClientId, founded);
        log.info("LawCases were attached to LawClient by the lawClientID : {}!", lawClientId);
        return ResponseEntity.ok(LawClientMapper.toDto(founded));
    }
}
