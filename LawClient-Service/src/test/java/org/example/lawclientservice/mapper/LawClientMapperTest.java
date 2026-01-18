package org.example.lawclientservice.mapper;

import org.example.lawclientservice.client.dto.LawCaseDto;
import org.example.lawclientservice.client.dto.LawClientRequestDto;
import org.example.lawclientservice.client.dto.LawClientResponseDto;
import org.example.lawclientservice.controller.mapper.LawClientMapper;
import org.example.lawclientservice.domain.LawCase;
import org.example.lawclientservice.domain.LawClient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LawClientMapperTest {

    @Test
    void toEntity_ShouldMapCorrectly() {
        // given
        LawCaseDto caseDto = LawCaseDto.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .build();

        LawClientRequestDto dto = LawClientRequestDto.builder()
                .name("Client Name")
                .lawCases(List.of(caseDto))
                .build();

        // when
        LawClient result = LawClientMapper.toEntity(dto);

        // then
        assertNotNull(result);
        assertEquals("Client Name", result.getName());
        assertNotNull(result.getLawCases());
        assertEquals(1, result.getLawCases().size());

        LawCase mappedCase = result.getLawCases().get(0);
        assertEquals(caseDto.getId(), mappedCase.getId());
        assertEquals(caseDto.getName(), mappedCase.getName());
        assertEquals(caseDto.getLawyerId(), mappedCase.getLawyerId());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {assertNull(LawClientMapper.toEntity(null));}

    @Test
    void toEntity_ShouldHandleNullLawCases() {
        LawClientRequestDto dto = LawClientRequestDto.builder()
                .name("Client Name")
                .lawCases(null)
                .build();

        LawClient result = LawClientMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("Client Name", result.getName());
        assertNotNull(result.getLawCases());
        assertTrue(result.getLawCases().isEmpty());
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        // given
        LawCase lawCase = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .build();

        LawClient entity = LawClient.builder()
                .id(UUID.randomUUID())
                .name("Client Name")
                .lawCases(List.of(lawCase))
                .build();

        // when
        LawClientResponseDto result = LawClientMapper.toDto(entity);

        // then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals("Client Name", result.getName());
        assertNotNull(result.getLawCases());
        assertEquals(1, result.getLawCases().size());

        LawCaseDto mappedCase = result.getLawCases().get(0);
        assertEquals(lawCase.getId(), mappedCase.getId());
        assertEquals(lawCase.getName(), mappedCase.getName());
        assertEquals(lawCase.getLawyerId(), mappedCase.getLawyerId());
    }


    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(LawClientMapper.toDto(null));
    }


    @Test
    void toDto_ShouldHandleNullLawCases() {
        LawClient entity = LawClient.builder()
                .id(UUID.randomUUID())
                .name("Client Name")
                .lawCases(null)
                .build();

        LawClientResponseDto result = LawClientMapper.toDto(entity);

        assertNotNull(result);
        assertEquals("Client Name", result.getName());
        assertNotNull(result.getLawCases());
        assertTrue(result.getLawCases().isEmpty());
    }
}
