package org.example.lawyerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.lawyerservice.client.dto.LawyerRequestDto;
import org.example.lawyerservice.client.dto.LawyerResponseDto;
import org.example.lawyerservice.controller.mapper.LawyerMapper;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.exception.LawyerNotFoundException;
import org.example.lawyerservice.service.LawyerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lawyers")
@Slf4j
@Tag(name = "Lawyer", description = "CRUD operations for law office lawyers")
@Validated
public class LawyerController {

    private final String DESCRIPTION_404_ID = "Lawyer was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String PATH_ID = "/{id}";

    private final LawyerService service;

    public LawyerController(LawyerService service) {this.service = service;}

    @Operation(summary = "Create a lawyer", description = "Creates a new lawyer in the database - POST /api/lawyers")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lawyer was created"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PostMapping
    public ResponseEntity<LawyerResponseDto> createLawyer(
            @Valid @RequestBody LawyerRequestDto requestDto) {

        log.info("POST /lawyers with name={}", requestDto.getName());

        Lawyer saved = service.createLawyer( LawyerMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create lawyer"));

        return ResponseEntity.status( HttpStatus.CREATED)
                .body(LawyerMapper.toDto(saved));
    }

    @Operation(summary = "Get Lawyer by Id", description = "Returns Lawyer data by the given Id " +
            "- GET /api/lawyers/by-id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer was delivered by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/by-id" + PATH_ID)
    public ResponseEntity<LawyerResponseDto> getLawyerById(@PathVariable UUID id) {
        log.info("GET /lawyers/{}", id);

        Lawyer lawyer = service.getLawyerById(id)
                .orElseThrow(() -> new LawyerNotFoundException ("Lawyer not found with id=" + id));

        return ResponseEntity.ok(LawyerMapper.toDto(lawyer));
    }

    @Operation(summary = "Get Lawyer by name", description = "Returns Lawyer data by the given name" +
            "- GET /api/lawyers/by-name/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer was delivered by name"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<LawyerResponseDto>> getLawyersByName(@PathVariable String name) {
        log.info("GET /lawyer/by-name/{}", name);

        List<LawyerResponseDto> lawyers = service.getLawyerByName(name)
                .stream()
                .map(LawyerMapper::toDto)
                .toList();

        return ResponseEntity.ok(lawyers);
    }

    @Operation(summary = "Get all Lawyers", description = "Returns a list of all Lawyers in the database" +
            "- GET /api/lawyers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer was delivered by name"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping
    public ResponseEntity<List<LawyerResponseDto>> getAllLawyers() {
        log.info("GET api/lawyers");

        List<LawyerResponseDto> clients = service.getAllLawyers()
                .stream()
                .map(LawyerMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Update lawyer", description = "Updates lawyer data by the given Id - GET /api/lawyers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer was updated by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PutMapping(PATH_ID)
    public ResponseEntity<LawyerResponseDto> updateLawyer(
            @PathVariable UUID  id,
            @Valid @RequestBody LawyerRequestDto requestDto) {

        log.info("PUT api/lawyers/{}", id);

        Lawyer updated = service.updateLawyerById(id, LawyerMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawyerNotFoundException("Cannot update. Lawyer not found with id=" + id));

        return ResponseEntity.ok(LawyerMapper.toDto(updated));
    }

    @Operation(summary = "Delete lawyer by Id", description = "Deletes the lawyer with the given ID from the database" +
            " - DELETE api/lawyers/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lawyer was deleted by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping(PATH_ID)
    public ResponseEntity<Void> deleteLawyer(@PathVariable UUID id) {
        log.info("DELETE api/lawyers/{}", id);

        service.deleteLawyerById(id)
                .orElseThrow(() -> new LawyerNotFoundException("Cannot delete. Lawyer not found with id =" + id));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all lawyers", description = "Deletes all lawyers from the database" +
            " - DELETE api/lawyers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete all Lawyers"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping
    public ResponseEntity<String> deleteAllLawyers() {
        log.warn("DELETE api/lawyers - deleting all lawyers");

        String result = service.deleteAllLawyers();
        return ResponseEntity.ok(result);
    }
}