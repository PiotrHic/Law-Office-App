package org.example.lawyerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.lawyerservice.client.dto.LawyerRequestDto;
import org.example.lawyerservice.client.dto.LawyerResponseDto;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.exception.LawyerNotFoundException;
import org.example.lawyerservice.service.LawyerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lawyers")
@Slf4j
@Tag(name = "Lawyer", description = "CRUD operations for law office lawyers")
@Validated
public class LawyerController {

    private final LawyerService service;

    public LawyerController(LawyerService service) {this.service = service;}

    @PostMapping
    @Operation(summary = "Create a lawyer", description = "Creates a new lawyer in the database")
    public ResponseEntity<LawyerResponseDto> createLawyer(
            @Valid @RequestBody LawyerRequestDto requestDto) {

        log.info("POST /lawyers with name={}", requestDto.getName());

        Lawyer saved = service.createLawyer(LawyerMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create lawyer"));

        return ResponseEntity.status( HttpStatus.CREATED)
                .body(LawyerMapper.toDto(saved));
    }

    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get lawyer by ID", description = "Returns lawyer data by the given ID")
    public ResponseEntity<LawyerResponseDto> getLawyerById(@PathVariable String id) {
        log.info("GET /lawyers/{}", id);

        Lawyer lawyer = service.getLawyerByID(id)
                .orElseThrow(() -> new LawyerNotFoundException ("Lawyer not found with id=" + id));

        return ResponseEntity.ok(LawyerMapper.toDto(lawyer));
    }

    @GetMapping("/by-name/{name}")
    @Operation(summary = "Get lawyers by name", description = "Returns a list of lawyers matching the given name")
    public ResponseEntity<List<LawyerResponseDto>> getLawyersByName( @PathVariable String name) {
        log.info("GET /lawyers/{}", name);

        List<LawyerResponseDto> lawyers = service.getLawyerByName(name)
                .stream()
                .map(LawyerMapper::toDto)
                .toList();

        return ResponseEntity.ok(lawyers);
    }

    @GetMapping
    @Operation(summary = "Get all lawyers", description = "Returns a list of all lawyers in the database")
    public ResponseEntity<List<LawyerResponseDto>> getAllLawyers() {
        log.info("GET /lawyers");

        List<LawyerResponseDto> clients = service.getAllLawyers()
                .stream()
                .map(LawyerMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update lawyer", description = "Updates lawyer data by the given ID")
    public ResponseEntity<LawyerResponseDto> updateLawyer(
            @PathVariable String id,
            @Valid @RequestBody LawyerRequestDto requestDto) {

        log.info("PUT /lawyers/{}", id);

        Lawyer updated = service.updateLawyerById(id, LawyerMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawyerNotFoundException("Cannot update. Lawyer not found with id=" + id));

        return ResponseEntity.ok(LawyerMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lawyer by ID", description = "Deletes the lawyer with the given ID from the database")
    public ResponseEntity<Void> deleteLawyer(@PathVariable String id) {
        log.info("DELETE /lawyers/{}", id);

        service.deleteLawyerById(id)
                .orElseThrow(() -> new LawyerNotFoundException("Cannot delete. Lawyer not found with id =" + id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all lawyers", description = "Deletes all lawyers from the database")
    public ResponseEntity<String> deleteAllLawyers() {
        log.warn("DELETE /lawyers - deleting all lawyers");

        String result = service.deleteAllLawyers();
        return ResponseEntity.ok(result);
    }
}