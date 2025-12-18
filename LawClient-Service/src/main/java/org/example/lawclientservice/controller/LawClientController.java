package org.example.lawclientservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.domain.dto.LawClientRequestDto;
import org.example.lawclientservice.domain.dto.LawClientResponseDto;
import org.example.lawclientservice.exception.LawClientNotFoundException;
import org.example.lawclientservice.service.LawClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Slf4j
@Tag(name = "LawClient", description = "CRUD operations for law office clients")
@Validated
public class LawClientController {

    private final LawClientService service;

    public LawClientController(LawClientService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a client", description = "Creates a new client in the database")
    public ResponseEntity<LawClientResponseDto> createClient(
            @Valid @RequestBody LawClientRequestDto requestDto) {

        log.info("POST /clients with name={}", requestDto.getName());

        LawClient saved = service.createClient(LawClientMapper.toEntity(requestDto))
                .orElseThrow(() -> new RuntimeException("Failed to create client"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LawClientMapper.toDto(saved));
    }

    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get client by ID", description = "Returns client data by the given ID")
    public ResponseEntity<LawClientResponseDto> getClientById(@PathVariable String id) {
        log.info("GET /clients/{}", id);

        LawClient client = service.getLawClientByID(id)
                .orElseThrow(() -> new LawClientNotFoundException ("LawClient not found with id=" + id));

        return ResponseEntity.ok(LawClientMapper.toDto(client));
    }

    @GetMapping("/by-name/{name}")
    @Operation(summary = "Get clients by name", description = "Returns a list of clients matching the given name")
    public ResponseEntity<List<LawClientResponseDto>> getClientsByName(@PathVariable String name) {
        log.info("GET /clients/{}", name);

        List<LawClientResponseDto> clients = service.getLawClientsByName(name)
                .stream()
                .map(LawClientMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Returns a list of all clients in the database")
    public ResponseEntity<List<LawClientResponseDto>> getAllClients() {
        log.info("GET /clients");

        List<LawClientResponseDto> clients = service.getAllLawClients()
                .stream()
                .map(LawClientMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client", description = "Updates client data by the given ID")
    public ResponseEntity<LawClientResponseDto> updateClient(
            @PathVariable String id,
            @Valid @RequestBody LawClientRequestDto requestDto) {

        log.info("PUT /clients/{}", id);

        LawClient updated = service.updateLawClientById(id, LawClientMapper.toEntity(requestDto))
                .orElseThrow(() -> new LawClientNotFoundException("Cannot update. LawClient not found with id=" + id));

        return ResponseEntity.ok(LawClientMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client by ID", description = "Deletes the client with the given ID from the database")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        log.info("DELETE /clients/{}", id);

        service.deleteLawClientById(id)
                .orElseThrow(() -> new LawClientNotFoundException("Cannot delete. LawClient not found with id=" + id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all clients", description = "Deletes all clients from the database")
    public ResponseEntity<String> deleteAllClients() {
        log.warn("DELETE /clients - deleting all clients");

        String result = service.deleteAllLawClients();
        return ResponseEntity.ok(result);
    }
}