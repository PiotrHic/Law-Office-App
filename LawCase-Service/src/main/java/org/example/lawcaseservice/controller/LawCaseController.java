package org.example.lawcaseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.lawcaseservice.client.dto.LawCaseRequestDto;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.exception.LawCaseNotFoundException;
import org.example.lawcaseservice.service.LawCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
@Slf4j
@Tag(name = "LawCase", description = "CRUD operations for law office lawCases")
@Validated
public class LawCaseController {

    private final String DESCRIPTION_404_ID = "LawCase was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String PATH_ID = "/{id}";

    private final LawCaseService service;

    public LawCaseController (LawCaseService service) {this.service = service;}

    @Operation(summary = "Create a LawCase", description = "Creates a new LawCase in the database " +
            "- POST /api/cases")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "LawCase was created"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PostMapping
    public ResponseEntity<LawCaseResponseDto> createLawCase(
            @Valid @RequestBody LawCaseRequestDto requestDto) {

        log.info("POST /lawCase with name={}", requestDto.getName());

        LawCase saved = service.createLawCase(LawCaseMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create lawCase"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LawCaseMapper.toDto(saved));
    }

    @Operation(summary = "Get lawCase by Id", description = "Get lawCase by Id " +
            "- GET /api/cases/by-id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCase was delivered by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/by-id" + PATH_ID)
    public ResponseEntity<LawCaseResponseDto> getLawCaseById(@PathVariable UUID id) {
        log.info("GET /api/cases/by-id/{}", id);

        LawCase lawyer = service.getLawCaseById(id)
                .orElseThrow(() -> new LawCaseNotFoundException("LawCase not found with id=" + id));

        return ResponseEntity.ok(LawCaseMapper.toDto(lawyer));
    }

    @Operation(summary = "Get LawCase by nam", description = "Returns a list of lawCases matching the given name " +
            "- GET /api/cases/by-name/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCase was delivered by name"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<LawCaseResponseDto>> getLawCasesByName(@PathVariable String name) {
        log.info("GET /api/cases/by-name/{}", name);

        List<LawCaseResponseDto> lawyers = service.getLawCaseByName(name)
                .stream()
                .map(LawCaseMapper::toDto)
                .toList();

        return ResponseEntity.ok(lawyers);
    }

    @Operation(summary = "Get all LawCases", description = "Returns a list of all LawCases in the database " +
            "- GET /api/cases")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCase was delivered by name"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping
    public ResponseEntity<List<LawCaseResponseDto>> getAllLawCases() {
        log.info("GET /api/cases");

        List<LawCaseResponseDto> clients = service.getAllLawCases()
                .stream()
                .map(LawCaseMapper::toDto)
                .collect( Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Update LawCase", description = "Updates LawCase data by the given Id " +
            "- PUT /api/cases")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCase was updated by id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PutMapping(PATH_ID)
    public ResponseEntity<LawCaseResponseDto> updateLawCase(
            @PathVariable UUID id,
            @Valid @RequestBody LawCaseRequestDto requestDto) {

        log.info("PUT /api/cases{}", id);

        LawCase updated = service.updateLawCaseById(id, LawCaseMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawCaseNotFoundException("Cannot update. LawCase not found with id=" + id));

        LawCaseResponseDto dto = LawCaseMapper.toDto(updated);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete LawCase by Id", description = "Deletes the LawCase with givenId from the database " +
            "- DELETE /api/cases/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LawCase was deleted by id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping(PATH_ID)
    public ResponseEntity<Void> deleteLawCase(@PathVariable UUID id) {
        log.info("DELETE /api/cases{}", id);

        service.deleteLawCaseById(id)
                .orElseThrow(() -> new LawCaseNotFoundException("Cannot delete. LawCase not found with id =" + id));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all LawCases", description = "Deletes all LawCases from the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete All LawCases"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping
    public ResponseEntity<String> deleteAllLawCases() {
        log.warn("DELETE /api/cases - deleting all LawCases");

        String result = service.deleteAllLawCases();
        return ResponseEntity.ok(result);
    }
}