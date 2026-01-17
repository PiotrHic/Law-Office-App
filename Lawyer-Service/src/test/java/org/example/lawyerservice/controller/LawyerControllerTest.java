package org.example.lawyerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lawyerservice.client.dto.LawyerRequestDto;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.service.LawyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(LawyerController.class)
class LawyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LawyerService lawyerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Lawyer sampleLawyer;

    @BeforeEach
    void setUp() {
        sampleLawyer = Lawyer.builder()
                .id(UUID.randomUUID())
                .name("John Smith")
                .lawCaseList(Collections.emptyList())
                .build();
    }

    // CREATE
    @Test
    void createLawyer_ShouldReturn201() throws Exception {
        LawyerRequestDto request = LawyerRequestDto.builder()
                .name("John Smith")
                .lawCases(Collections.emptyList())
                .build();

        when(lawyerService.createLawyer(any())).thenReturn(Optional.of(sampleLawyer));

        mockMvc.perform(post("/api/lawyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    // GET BY ID
    @Test
    void getLawyerById_ShouldReturn200() throws Exception {
        when(lawyerService.getLawyerById(sampleLawyer.getId())).thenReturn(Optional.of(sampleLawyer));

        mockMvc.perform(get("/api/lawyers/by-id/{id}", sampleLawyer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleLawyer.getId().toString()))
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    @Test
    void getLawyerById_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(lawyerService.getLawyerById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lawyers/by-id/{id}", id))
                .andExpect(status().isNotFound());
    }

    // GET BY NAME
    @Test
    void getLawyersByName_ShouldReturn200() throws Exception {
        when(lawyerService.getLawyerByName("John"))
                .thenReturn(List.of(sampleLawyer));

        mockMvc.perform(get("/api/lawyers/by-name/{name}", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Smith"));
    }

    // GET ALL
    @Test
    void getAllLawyers_ShouldReturn200() throws Exception {
        when(lawyerService.getAllLawyers()).thenReturn(List.of(sampleLawyer));

        mockMvc.perform(get("/api/lawyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleLawyer.getId().toString()))
                .andExpect(jsonPath("$[0].name").value("John Smith"));
    }

    // UPDATE
    @Test
    void updateLawyer_ShouldReturn200() throws Exception {
        LawyerRequestDto request = LawyerRequestDto.builder()
                .name("John Updated")
                .lawCases(Collections.emptyList())
                .build();

        Lawyer updatedLawyer = Lawyer.builder()
                .id(sampleLawyer.getId())
                .name("John Updated")
                .lawCaseList(Collections.emptyList())
                .build();

        when(lawyerService.updateLawyerById(eq(sampleLawyer.getId()), any()))
                .thenReturn(Optional.of(updatedLawyer));

        mockMvc.perform(put("/api/lawyers/{id}", sampleLawyer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void updateLawyer_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();

        LawyerRequestDto request = LawyerRequestDto.builder()
                .name("New Name")
                .lawCases(Collections.emptyList())
                .build();

        when(lawyerService.updateLawyerById(eq(id), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/lawyers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // DELETE ONE
    @Test
    void deleteLawyer_ShouldReturn204() throws Exception {
        when(lawyerService.deleteLawyerById(sampleLawyer.getId()))
                .thenReturn(Optional.ofNullable(sampleLawyer));

        mockMvc.perform(delete("/api/lawyers/{id}", sampleLawyer.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLawyer_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(lawyerService.deleteLawyerById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/lawyers/{id}", id))
                .andExpect(status().isNotFound());
    }

    // DELETE ALL
    @Test
    void deleteAllLawyers_ShouldReturn200() throws Exception {
        when(lawyerService.deleteAllLawyers()).thenReturn("All deleted");

        mockMvc.perform(delete("/api/lawyers"))
                .andExpect(status().isOk())
                .andExpect(content().string("All deleted"));
    }
}