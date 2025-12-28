package org.example.lawyerservice.controller;

import org.example.lawyerservice.controller.dto.LawCaseDto;
import org.example.lawyerservice.controller.dto.LawyerRequestDto;
import org.example.lawyerservice.controller.dto.LawyerResponseDto;
import org.example.lawyerservice.domain.LawCase;
import org.example.lawyerservice.domain.Lawyer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LawyerMapper {

    // === Request DTO → Entity ===
    public static Lawyer toEntity(LawyerRequestDto dto) {
        if (dto == null) return null;

        List<LawCase> lawCases = new ArrayList<>();
        if (dto.getLawCases() != null) {
            lawCases = dto.getLawCases().stream()
                    .map(LawyerMapper::toEntity)
                    .collect(Collectors.toList());
        }

        return Lawyer.builder()
                .name(dto.getName())
                .lawCaseList(lawCases)
                .build();
    }

    private static LawCase toEntity(LawCaseDto dto) {
        if (dto == null) return null;

        return LawCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lawyerId(dto.getLawyerId())
                .build();
    }

    // === Entity → Response DTO ===
    public static LawyerResponseDto toDto(Lawyer entity){
        if (entity == null) return null;

        List<LawCaseDto> lawCases = new ArrayList<>();
        if (entity.getLawCaseList() != null) {
            lawCases = entity.getLawCaseList().stream()
                    .map(LawyerMapper::toDto)
                    .collect(Collectors.toList());
        }

        return LawyerResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lawCases(lawCases)
                .build();
    }

    private static LawCaseDto toDto(LawCase entity) {
        if (entity == null) return null;

        return LawCaseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lawyerId(entity.getLawyerId())
                .build();
    }
}