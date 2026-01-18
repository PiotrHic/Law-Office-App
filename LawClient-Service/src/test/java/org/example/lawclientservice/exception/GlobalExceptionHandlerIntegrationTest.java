package org.example.lawclientservice.exception;

import org.example.lawclientservice.controller.LawClientController;
import org.example.lawclientservice.service.LawClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(LawClientController.class)
class LawClientControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawClientService service;

    // 1️⃣ 404 — LawClientNotFoundException
    @Test
    void whenClientNotFound_thenReturns404() throws Exception {
        UUID fakeId = UUID.randomUUID();

        when(service.getLawClientByID(fakeId))
                .thenThrow(new LawClientNotFoundException("LawClient not found id=" + fakeId));

        mockMvc.perform(get("/api/clients/by-id/" + fakeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("LawClient not found id=" + fakeId))
                .andExpect(jsonPath("$.path").value("/api/clients/by-id/" + fakeId));
    }


    // 2️⃣ 400 — błędny JSON (walidacja DTO)
    @Test
    void whenInvalidDTO_thenReturns400() throws Exception {
        String invalidJson = "{\"name\": \"\"}"; // name nie może być pusty

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.message").value("Invalid request data"));
    }


    // 3️⃣ 400 — ConstraintViolationException
    @Test
    void whenConstraintViolation_thenReturns400() throws Exception {
        UUID fakeId = UUID.randomUUID();

        doThrow(new ConstraintViolationException("Invalid ID", null))
                .when(service).deleteLawClientById(fakeId);

        mockMvc.perform(delete("/api/clients/" + fakeId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Constraint violation"))
                .andExpect(jsonPath("$.message").value("Invalid ID"))
                .andExpect(jsonPath("$.path").value("/api/clients/" + fakeId));
    }


    // 4️⃣ 500 — nieobsłużony wyjątek
    @Test
    void whenUnhandledException_thenReturns500() throws Exception {
        UUID fakeId = UUID.randomUUID();

        when(service.getLawClientByID(fakeId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/clients/by-id/" + fakeId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/clients/by-id/" + fakeId));
    }
}
