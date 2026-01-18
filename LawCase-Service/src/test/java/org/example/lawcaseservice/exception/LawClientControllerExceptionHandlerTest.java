package org.example.lawcaseservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.lawcaseservice.controller.LawCaseController;
import org.example.lawcaseservice.service.LawCaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LawCaseController.class)
public class LawClientControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawCaseService service;

    // 1️⃣ 404 — LawCaseNotFoundException
    @Test
    void whenClientNotFound_thenReturns404() throws Exception {
        UUID fakeId = UUID.randomUUID();

        when(service.getLawCaseById(fakeId))
                .thenThrow(new LawCaseNotFoundException("LawCase not found id=" + fakeId));

        mockMvc.perform(get("/api/cases/by-id/" + fakeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("LawCase not found id=" + fakeId))
                .andExpect(jsonPath("$.path").value("/api/cases/by-id/" + fakeId));
    }

    // 2️⃣ 400 — wrongJSON)
    @Test
    void whenInvalidDTO_thenReturns400() throws Exception {
        String invalidJson = "{\"name\": \"\"}";

        mockMvc.perform(post("/api/cases")
                        .contentType( MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.message").value("Invalid request data"));
    }

    @Test
    void whenConstraintViolation_thenReturns400() throws Exception {
        UUID fakeId = UUID.randomUUID();

        doThrow(new ConstraintViolationException ("Invalid ID", null))
                .when(service).deleteLawCaseById(fakeId);

        mockMvc.perform(delete("/api/cases/" + fakeId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Constraint violation"))
                .andExpect(jsonPath("$.message").value("Invalid ID"))
                .andExpect(jsonPath("$.path").value("/api/cases/" + fakeId));
    }

    // 4️⃣ 500
    @Test
    void whenUnhandledException_thenReturns500() throws Exception {
        UUID fakeId = UUID.randomUUID();

        when(service.getLawCaseById(fakeId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/cases/by-id/" + fakeId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/cases/by-id/" + fakeId));
    }
}
