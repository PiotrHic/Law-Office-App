package org.example.lawclientservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.lawclientservice.controller.mapper.LawClientMapper;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.client.dto.LawClientRequestDto;
import org.example.lawclientservice.client.dto.LawClientResponseDto;
import org.example.lawclientservice.exception.LawClientNotFoundException;
import org.example.lawclientservice.service.LawClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Slf4j
@Tag(name = "LawClient", description = "CRUD operations for law office clients")
@Validated
public class LawClientController {

    private final String DESCRIPTION_404_ID = "LawClient was not found by id";
    private final String DESCRIPTION_500_SHORT = "Some internal server error";
    private final String PATH_ID = "/{id}";

    private final LawClientService service;

    public LawClientController(LawClientService service) {
        this.service = service;
    }

    @Operation(summary = "Create a Client", description = "Creates a new client in the database " +
            "- POST /api/clients")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "LawClient was created"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PostMapping
    public ResponseEntity<LawClientResponseDto> createClient(
            @Valid @RequestBody LawClientRequestDto requestDto) {

        log.info("POST api/clients with name={}", requestDto.getName());

        LawClient saved = service.createClient( LawClientMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create client"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LawClientMapper.toDto(saved));
    }

    @Operation(summary = "Get Client by ID", description = "Returns client data by the given " +
            "- GET /api/clients/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client was founded by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @GetMapping("/by-id/{id}")
    public ResponseEntity<LawClientResponseDto> getClientById(@PathVariable UUID id) {
        log.info("GET /clients/{}", id);

        LawClient client = service.getLawClientByID(id)
                .orElseThrow(() -> new LawClientNotFoundException ("LawClient not found with id=" + id));

        return ResponseEntity.ok(LawClientMapper.toDto(client));
    }

    @GetMapping("/by-name/{name}")
    @Operation(summary = "Get clients by name", description = "Returns a list of clients " +
            "matching the given name - GET /api/clients/by-name/{name}")
    public ResponseEntity<List<LawClientResponseDto>> getClientsByName(@PathVariable String name) {
        log.info("GET api/clients/by-name/{}", name);

        List<LawClientResponseDto> clients = service.getLawClientsByName(name)
                .stream()
                .map(LawClientMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Returns a list of all clients in the database")
    public ResponseEntity<List<LawClientResponseDto>> getAllClients() {
        log.info("GET api/clients");

        List<LawClientResponseDto> clients = service.getAllLawClients()
                .stream()
                .map(LawClientMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Update Client", description = "Updates Client data by the " +
            "Id - PUT /api/clients/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client was updated by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @PutMapping(PATH_ID)
    public ResponseEntity<LawClientResponseDto> updateClient(
            @PathVariable UUID id,
            @Valid @RequestBody LawClientRequestDto requestDto) {

        log.info("PUT api/clients/{}", id);

        LawClient updated = service.updateLawClientById(id, LawClientMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawClientNotFoundException("Cannot update. LawClient not found with id=" + id));

        return ResponseEntity.ok(LawClientMapper.toDto(updated));
    }

    @Operation(summary = "Delete Client", description = "Deletes Client data by the " +
            "Id - DELETE /api/clients/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientwas updated by Id"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping(PATH_ID)
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        log.info("DELETE /clients/{}", id);

        service.deleteLawClientById(id)
                .orElseThrow(() -> new LawClientNotFoundException("Cannot delete. LawClient not found with id=" + id));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all Clients", description = "Deletes all Clients from the database" +
            " - DELETE api/clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete all Clients"),
            @ApiResponse(responseCode = "404", description = DESCRIPTION_404_ID),
            @ApiResponse(responseCode = "500", description = DESCRIPTION_500_SHORT)
    })
    @DeleteMapping

    public ResponseEntity<String> deleteAllClients() {
        log.warn("DELETE api/clients - deleting all clients");

        String result = service.deleteAllLawClients();
        return ResponseEntity.ok(result);
    }
}