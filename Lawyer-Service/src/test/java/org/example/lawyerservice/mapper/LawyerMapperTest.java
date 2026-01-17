package org.example.lawyerservice.mapper;

import org.example.lawyerservice.client.dto.LawCaseDto;
import org.example.lawyerservice.client.dto.LawyerRequestDto;
import org.example.lawyerservice.client.dto.LawyerResponseDto;
import org.example.lawyerservice.controller.mapper.LawyerMapper;
import org.example.lawyerservice.domain.LawCase;
import org.example.lawyerservice.domain.Lawyer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

class LawyerMapperTest {

    @Test
    void toEntity_ShouldMapCorrectly() {
        // given
        LawCaseDto caseDto = LawCaseDto.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .build();

        LawyerRequestDto dto = LawyerRequestDto.builder()
                .name("John Smith")
                .lawCases(List.of(caseDto))
                .build();

        // when
        Lawyer result = LawyerMapper.toEntity(dto);

        // then
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertNotNull(result.getLawCaseList());
        assertEquals(1, result.getLawCaseList().size());

        LawCase mappedCase = result.getLawCaseList().get(0);
        assertEquals(caseDto.getId(), mappedCase.getId());
        assertEquals(caseDto.getName(), mappedCase.getName());
        assertEquals(caseDto.getLawyerId(), mappedCase.getLawyerId());
    }


    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(LawyerMapper.toEntity(null));
    }


    @Test
    void toEntity_ShouldHandleNullLawCases() {
        LawyerRequestDto dto = LawyerRequestDto.builder()
                .name("John Smith")
                .lawCases(null)
                .build();

        Lawyer result = LawyerMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertNotNull(result.getLawCaseList());
        assertTrue(result.getLawCaseList().isEmpty());
    }


    @Test
    void toDto_ShouldMapCorrectly() {
        // given
        LawCase lawCase = LawCase.builder()
                .id(UUID.randomUUID())
                .name("Case A")
                .lawyerId(UUID.randomUUID())
                .build();

        Lawyer entity = Lawyer.builder()
                .id(UUID.randomUUID())
                .name("John Smith")
                .lawCaseList(List.of(lawCase))
                .build();

        // when
        LawyerResponseDto result = LawyerMapper.toDto(entity);

        // then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals("John Smith", result.getName());
        assertNotNull(result.getLawCases());
        assertEquals(1, result.getLawCases().size());

        LawCaseDto mappedCase = result.getLawCases().get(0);
        assertEquals(lawCase.getId(), mappedCase.getId());
        assertEquals(lawCase.getName(), mappedCase.getName());
        assertEquals(lawCase.getLawyerId(), mappedCase.getLawyerId());
    }


    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(LawyerMapper.toDto(null));
    }


    @Test
    void toDto_ShouldHandleNullLawCases() {
        Lawyer entity = Lawyer.builder()
                .id(UUID.randomUUID())
                .name("John Smith")
                .lawCaseList(null)
                .build();

        LawyerResponseDto result = LawyerMapper.toDto(entity);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertNotNull(result.getLawCases());
        assertTrue(result.getLawCases().isEmpty());
    }
}