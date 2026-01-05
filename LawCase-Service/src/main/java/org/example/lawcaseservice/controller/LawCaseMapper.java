package org.example.lawcaseservice.controller;

import org.example.lawcaseservice.client.dto.LawCaseRequestDto;
import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.domain.LawCase;

public class LawCaseMapper {

    // === Request DTO → Entity ===
    public static LawCase toEntity( LawCaseRequestDto dto) {
        if (dto == null) return null;

        return LawCase.builder()
                .name(dto.getName())
                .lawClientId(dto.getLawClientId())
                .lawClient(dto.getLawClient())
                .lawyerId(dto.getLawyerId())
                .lawyer(dto.getLawyer())
                .build();
    }

    // === Entity → Response DTO ===
    public static LawCaseResponseDto toDto(LawCase entity){
        if (entity == null) return null;

        return LawCaseResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lawClientId(entity.getLawClientId())
                .lawClient(entity.getLawClient())
                .lawyerId(entity.getLawyerId())
                .lawyer(entity.getLawyer())
                .build();
    }

}