package org.example.lawyerservice.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import jakarta.validation.ConstraintViolationException;

import org.example.lawyerservice.controller.LawyerController;
import org.example.lawyerservice.service.LawyerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(LawyerController.class)
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawyerService service;

    // 1️⃣ Test dla 404 – LawyerNotFoundException
    @Test
    void whenLawyerNotFound_thenReturns404() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(service.getLawyerById(fakeId))
                .thenThrow(new LawyerNotFoundException("Lawyer not found id=" + fakeId));

        mockMvc.perform(get("/api/lawyers/by-id/" + fakeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Lawyer not found id=" + fakeId))
                .andExpect(jsonPath("$.path").value("/api/lawyers/by-id/" + fakeId));
    }

    @Test
    void whenInvalidDTO_thenReturns400() throws Exception {
        String invalidJson = "{\"name\": \"\"}"; // załóżmy, że name nie może być pusty

        mockMvc.perform(post("/api/lawyers")
                        .contentType(MediaType.APPLICATION_JSON)
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
        doThrow(new ConstraintViolationException("Invalid ID", null))
                .when(service).deleteLawyerById(fakeId);

        mockMvc.perform(delete("/api/lawyers/" + fakeId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Constraint violation"))
                .andExpect(jsonPath("$.message").value("Invalid ID"))
                .andExpect(jsonPath("$.path").value("/api/lawyers/" + fakeId));
    }

    @Test
    void whenUnhandledException_thenReturns500() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(service.getLawyerById(fakeId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/lawyers/by-id/" + fakeId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/lawyers/by-id/" + fakeId));
    }

}
