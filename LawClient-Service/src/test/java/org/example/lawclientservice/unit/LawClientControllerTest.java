package org.example.lawclientservice.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawclientservice.controller.LawClientController;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.domain.dto.LawClientRequestDto;
import org.example.lawclientservice.exception.GlobalExceptionHandler;
import org.example.lawclientservice.service.LawClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LawClientController.class)
@Import(GlobalExceptionHandler.class)
class LawClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LawClientService service;

    // ========= CREATE =========

    @Test
    void shouldCreateClient() throws Exception {
        LawClientRequestDto request = new LawClientRequestDto();
        request.setName("Jan Kowalski");

        LawClient saved = new LawClient();
        saved.setId("123");
        saved.setName("Jan Kowalski");

        when(service.createClient(any()))
                .thenReturn(Optional.of(saved));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Jan Kowalski"));
    }

    // ========= VALIDATION =========

    @Test
    void shouldReturn400_whenValidationFails() throws Exception {
        String invalidJson = "{\"name\": \"\"}";

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    // ========= READ BY ID =========

    @Test
    void shouldReturnClientById() throws Exception {
        LawClient client = new LawClient();
        client.setId("123");
        client.setName("Jan Kowalski");

        when(service.getLawClientByID("123"))
                .thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/by-id/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Jan Kowalski"));
    }

    @Test
    void shouldReturn404_whenClientNotFoundById() throws Exception {
        when(service.getLawClientByID("404"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/by-id/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    // ========= READ BY NAME =========

    @Test
    void shouldReturnClientsByName() throws Exception {
        LawClient c1 = new LawClient();
        c1.setName("Jan");

        LawClient c2 = new LawClient();
        c2.setName("Jan");

        when(service.getLawClientsByName("Jan"))
                .thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/clients/by-name/Jan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ========= READ ALL =========

    @Test
    void shouldReturnAllClients() throws Exception {
        when(service.getAllLawClients())
                .thenReturn(List.of(new LawClient(), new LawClient()));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ========= UPDATE =========

    @Test
    void shouldUpdateClient() throws Exception {
        LawClientRequestDto request = new LawClientRequestDto();
        request.setName("New Name");

        LawClient updated = new LawClient();
        updated.setId("123");
        updated.setName("New Name");

        when(service.updateLawClientById(eq("123"), any()))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/clients/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    // ========= DELETE =========

    @Test
    void shouldDeleteClient() throws Exception {
        when(service.deleteLawClientById("123"))
                .thenReturn(Optional.of(new LawClient()));

        mockMvc.perform(delete("/api/clients/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenDeleteNonExistingClient() throws Exception {
        when(service.deleteLawClientById("404"))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clients/404"))
                .andExpect(status().isNotFound());
    }
}