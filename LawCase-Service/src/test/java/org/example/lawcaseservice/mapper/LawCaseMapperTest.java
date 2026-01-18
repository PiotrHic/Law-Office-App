package org.example.lawcaseservice.mapper;

import org.example.lawcaseservice.client.dto.LawCaseRequestDto;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.controller.mapper.LawCaseMapper;
import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.domain.LawClient;
import org.example.lawcaseservice.domain.Lawyer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LawCaseMapperTest {

    @Test
    void toDto_shouldMapEntityToDto() {
        // given
        UUID lawyerId = UUID.randomUUID();
        UUID lawClientId = UUID.randomUUID();

        LawCase lawCase = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Test Case")
                .lawyerId(lawyerId)
                .lawyer(new Lawyer(UUID.randomUUID(), "Jane Smith 1"))
                .lawClient(new LawClient(UUID.randomUUID(), "Jane Smith 2"))
                .lawClientId(lawClientId)
                .build();

        // when
        LawCaseResponseDto dto = LawCaseMapper.toDto(lawCase);

        // then
        assertNotNull(dto);
        assertEquals(lawCase.getId(), dto.getId());
        assertEquals(lawCase.getName(), dto.getName());
        assertEquals(lawCase.getLawyerId(), dto.getLawyerId());
        assertEquals(lawCase.getLawyer(), dto.getLawyer());
        assertEquals(lawCase.getLawClient(), dto.getLawClient());
        assertEquals(lawCase.getLawClientId(), dto.getLawClientId());
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        // given
        UUID lawyerId = UUID.randomUUID();
        UUID lawClientId = UUID.randomUUID();

        LawCaseRequestDto dto = LawCaseRequestDto.builder()
                .name("Dto Case")
                .lawyerId(lawyerId)
                .lawyer(new Lawyer(UUID.randomUUID(), "Jane Smith 1"))
                .lawClient(new LawClient(UUID.randomUUID(), "Jane Smith 2"))
                .lawClientId(lawClientId)
                .build();

        // when
        LawCase entity = LawCaseMapper.toEntity(dto);

        // then
        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getLawyerId(), entity.getLawyerId());
        assertEquals(dto.getLawyer(), entity.getLawyer());
        assertEquals(dto.getLawClient(), entity.getLawClient());
        assertEquals(dto.getLawClientId(), entity.getLawClientId());
    }

    @Test
    void toDto_shouldReturnNull_whenEntityIsNull() {
        assertNull(LawCaseMapper.toDto(null));
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        assertNull(LawCaseMapper.toEntity(null));
    }
}
