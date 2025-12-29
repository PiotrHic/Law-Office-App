package org.example.lawclientservice.unit;

import org.example.lawclientservice.controller.LawClientMapper;
import org.example.lawclientservice.domain.LawCase;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.client.dto.LawCaseDto;
import org.example.lawclientservice.client.dto.LawClientRequestDto;
import org.example.lawclientservice.client.dto.LawClientResponseDto;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LawClientMapperTest {

    // ========= REQUEST DTO → ENTITY =========

    @Test
    void shouldMapRequestDtoToEntity_withoutLawCases() {
        // given
        LawClientRequestDto dto = LawClientRequestDto.builder()
                .name("Jan Kowalski")
                .build();

        // when
        LawClient entity = LawClientMapper.toEntity(dto);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Jan Kowalski");
        assertThat(entity.getLawCases()).isEmpty();
    }

    @Test
    void shouldMapRequestDtoToEntity_withLawCases() {
        // given
        LawCaseDto caseDto = LawCaseDto.builder()
                .id("c1")
                .name("Case 1")
                .lawyerId("l1")
                .build();

        LawClientRequestDto dto = LawClientRequestDto.builder()
                .name("Jan Kowalski")
                .lawCases(List.of(caseDto))
                .build();

        // when
        LawClient entity = LawClientMapper.toEntity(dto);

        // then
        assertThat(entity.getLawCases()).hasSize(1);

        LawCase lawCase = entity.getLawCases().iterator().next();
        assertThat(lawCase.getId()).isEqualTo("c1");
        assertThat(lawCase.getName()).isEqualTo("Case 1");
        assertThat(lawCase.getLawyerId()).isEqualTo("l1");
    }

    // ========= ENTITY → RESPONSE DTO =========

    @Test
    void shouldMapEntityToResponseDto_withoutLawCases() {
        // given
        LawClient entity = LawClient.builder()
                .id("123")
                .name("Jan Kowalski")
                .build();

        // when
        LawClientResponseDto dto = LawClientMapper.toDto(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("123");
        assertThat(dto.getName()).isEqualTo("Jan Kowalski");
        assertThat(dto.getLawCases()).isEmpty();
    }

    @Test
    void shouldMapEntityToResponseDto_withLawCases() {
        // given
        LawCase lawCase = LawCase.builder()
                .id("c1")
                .name("Case 1")
                .lawyerId("l1")
                .build();

        LawClient entity = LawClient.builder()
                .id("123")
                .name("Jan Kowalski")
                .lawCases(List.of(lawCase))
                .build();

        // when
        LawClientResponseDto dto = LawClientMapper.toDto(entity);

        // then
        assertThat(dto.getLawCases()).hasSize(1);

        LawCaseDto caseDto = dto.getLawCases().iterator().next();
        assertThat(caseDto.getId()).isEqualTo("c1");
        assertThat(caseDto.getName()).isEqualTo("Case 1");
        assertThat(caseDto.getLawyerId()).isEqualTo("l1");
    }

    // ========= NULL SAFETY =========

    @Test
    void shouldReturnNull_whenMappingNull() {
        assertThat(LawClientMapper.toEntity(null)).isNull();
        assertThat(LawClientMapper.toDto(null)).isNull();
    }
}