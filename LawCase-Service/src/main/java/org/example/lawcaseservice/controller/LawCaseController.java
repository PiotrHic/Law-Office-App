package org.example.lawcaseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
@Slf4j
@Tag(name = "LawCase", description = "CRUD operations for law office lawCases")
@Validated
public class LawCaseController {

    private final LawCaseService service;

    public LawCaseController (LawCaseService service) {this.service = service;}

    @PostMapping
    @Operation(summary = "Create a lawCase", description = "Creates a new lawCase in the database")
    public ResponseEntity<LawCaseResponseDto> createLawCase(
            @Valid @RequestBody LawCaseRequestDto requestDto) {

        log.info("POST /lawCase with name={}", requestDto.getName());

        LawCase saved = service.createLawCase(LawCaseMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create lawCase"));

        return ResponseEntity.status( HttpStatus.CREATED)
                .body(LawCaseMapper.toDto(saved));
    }

    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get lawCase by ID", description = "Returns lawCase data by the given ID")
    public ResponseEntity<LawCaseResponseDto> getLawCaseById(@PathVariable String id) {
        log.info("GET /lawCase/{}", id);

        LawCase lawyer = service.getLawCaseById(id)
                .orElseThrow(() -> new LawCaseNotFoundException("LawCase not found with id=" + id));

        return ResponseEntity.ok(LawCaseMapper.toDto(lawyer));
    }

    @GetMapping("/by-name/{name}")
    @Operation(summary = "Get lawCase by name", description = "Returns a list of lawCases matching the given name")
    public ResponseEntity<List<LawCaseResponseDto>> getLawCasesByName(@PathVariable String name) {
        log.info("GET /lawCases/{}", name);

        List<LawCaseResponseDto> lawyers = service.getLawCaseByName(name)
                .stream()
                .map(LawCaseMapper::toDto)
                .toList();

        return ResponseEntity.ok(lawyers);
    }

    @GetMapping
    @Operation(summary = "Get all lawCases", description = "Returns a list of all lawCases in the database")
    public ResponseEntity<List<LawCaseResponseDto>> getAllLawCases() {
        log.info("GET /lawCases");

        List<LawCaseResponseDto> clients = service.getAllLawCases()
                .stream()
                .map(LawCaseMapper::toDto)
                .collect( Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update lawCase", description = "Updates lawCase data by the given ID")
    public ResponseEntity<LawCaseResponseDto> updateLawCase(
            @PathVariable String id,
            @Valid @RequestBody LawCaseRequestDto requestDto) {

        log.info("PUT /lawyers/{}", id);

        LawCase updated = service.updateLawCaseById(id, LawCaseMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawCaseNotFoundException("Cannot update. LawCase not found with id=" + id));

        return ResponseEntity.ok(LawCaseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lawCase by ID", description = "Deletes the lawCase with the given ID from the database")
    public ResponseEntity<Void> deleteLawCase(@PathVariable String id) {
        log.info("DELETE /lawCases/{}", id);

        service.deleteLawCaseById(id)
                .orElseThrow(() -> new LawCaseNotFoundException("Cannot delete. LawCase not found with id =" + id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all lawCases", description = "Deletes all lawCases from the database")
    public ResponseEntity<String> deleteAllLawCases() {
        log.warn("DELETE /lawCases - deleting all lawCases");

        String result = service.deleteAllLawCases();
        return ResponseEntity.ok(result);
    }
}