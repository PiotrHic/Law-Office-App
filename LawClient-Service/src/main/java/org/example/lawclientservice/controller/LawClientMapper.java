package org.example.lawclientservice.controller;



import org.example.lawclientservice.domain.LawCase;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.domain.dto.LawCaseDto;
import org.example.lawclientservice.domain.dto.LawClientRequestDto;
import org.example.lawclientservice.domain.dto.LawClientResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LawClientMapper {

    // === Request DTO → Entity ===
    public static LawClient toEntity( LawClientRequestDto dto) {
        if (dto == null) return null;

        List<LawCase> lawCases = new ArrayList<>();
        if (dto.getLawCases() != null) {
            lawCases = dto.getLawCases().stream()
                    .map(LawClientMapper::toEntity)
                    .collect(Collectors.toList());
        }

        return LawClient.builder()
                .name(dto.getName())
                .lawCases(lawCases)
                .build();
    }

    private static LawCase toEntity( LawCaseDto dto) {
        if (dto == null) return null;

        return LawCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lawyerId(dto.getLawyerId())
                .build();
    }

    // === Entity → Response DTO ===
    public static LawClientResponseDto toDto( LawClient entity) {
        if (entity == null) return null;

        List<LawCaseDto> lawCases = new ArrayList<>();
        if (entity.getLawCases() != null) {
            lawCases = entity.getLawCases().stream()
                    .map(LawClientMapper::toDto)
                    .collect(Collectors.toList());
        }

        return LawClientResponseDto.builder()
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