package org.example.lawclientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawclientservice.client.dto.LawClientRequestDto;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.service.LawClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LawClientController.class)
class LawClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawClientService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    private LawClient sampleClient;

    @BeforeEach
    void setUp() {
        sampleClient = LawClient.builder()
                .id(UUID.randomUUID())
                .name("Adam Nowak")
                .lawCases(Collections.emptyList())
                .build();
    }

    // CREATE
    @Test
    void createClient_ShouldReturn201() throws Exception {
        LawClientRequestDto request = LawClientRequestDto.builder()
                .name("Adam Nowak")
                .lawCases(Collections.emptyList())
                .build();

        when(service.createClient(any())).thenReturn(Optional.of(sampleClient));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Adam Nowak"));
    }

    // GET BY ID
    @Test
    void getClientById_ShouldReturn200() throws Exception {
        when(service.getLawClientByID(sampleClient.getId())).thenReturn(Optional.of(sampleClient));

        mockMvc.perform(get("/api/clients/by-id/{id}", sampleClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleClient.getId().toString()))
                .andExpect(jsonPath("$.name").value("Adam Nowak"));
    }

    @Test
    void getClientById_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getLawClientByID(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/by-id/{id}", id))
                .andExpect(status().isNotFound());
    }

    // GET BY NAME
    @Test
    void getClientsByName_ShouldReturn200() throws Exception {
        when(service.getLawClientsByName("Adam")).thenReturn(List.of(sampleClient));

        mockMvc.perform(get("/api/clients/by-name/{name}", "Adam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Adam Nowak"));
    }

    // GET ALL
    @Test
    void getAllClients_ShouldReturn200() throws Exception {
        when(service.getAllLawClients()).thenReturn(List.of(sampleClient));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleClient.getId().toString()))
                .andExpect(jsonPath("$[0].name").value("Adam Nowak"));
    }

    // UPDATE
    @Test
    void updateClient_ShouldReturn200() throws Exception {
        LawClientRequestDto request = LawClientRequestDto.builder()
                .name("Adam Updated")
                .lawCases(Collections.emptyList())
                .build();

        LawClient updatedClient = LawClient.builder()
                .id(sampleClient.getId())
                .name("Adam Updated")
                .lawCases(Collections.emptyList())
                .build();

        when(service.updateLawClientById(eq(sampleClient.getId()), any()))
                .thenReturn(Optional.of(updatedClient));

        mockMvc.perform(put("/api/clients/{id}", sampleClient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Adam Updated"));
    }

    @Test
    void updateClient_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        LawClientRequestDto request = LawClientRequestDto.builder()
                .name("New Name")
                .lawCases(Collections.emptyList())
                .build();

        when(service.updateLawClientById(eq(id), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // DELETE ONE
    @Test
    void deleteClient_ShouldReturn204() throws Exception {
        when(service.deleteLawClientById(sampleClient.getId()))
                .thenReturn(Optional.of(sampleClient));

        mockMvc.perform(delete("/api/clients/{id}", sampleClient.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteClient_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.deleteLawClientById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clients/{id}", id))
                .andExpect(status().isNotFound());
    }

    // DELETE ALL
    @Test
    void deleteAllClients_ShouldReturn200() throws Exception {
        when(service.deleteAllLawClients()).thenReturn("All clients deleted");

        mockMvc.perform(delete("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string("All clients deleted"));
    }
}