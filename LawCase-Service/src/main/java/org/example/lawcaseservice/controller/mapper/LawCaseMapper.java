package org.example.lawcaseservice.controller.mapper;

import org.example.lawcaseservice.client.dto.LawCaseResponseDto;
import org.example.lawcaseservice.domain.LawCase;
import org.springframework.stereotype.Component;

@Component
public class LawCaseMapper {

    public static LawCaseResponseDto toDTO( LawCase lawcase) {
        if(lawcase == null) {
            return null;
        }
        return LawCaseResponseDto.builder()
                .id(lawcase.getId())
                .name(lawcase.getName())
                .lawyerId(lawcase.getLawyerId())
                .lawyer(lawcase.getLawyer())
                .lawClient(lawcase.getLawClient())
                .lawClientId(lawcase.getLawClientId())
                .build();
    }

    public static LawCase toEntity(LawCaseResponseDto dto) {
        if(dto == null) {
            return null;
        }
        return LawCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lawyerId(dto.getLawyerId())
                .lawyer(dto.getLawyer())
                .lawClient(dto.getLawClient())
                .lawClientId(dto.getLawClientId())
                .build();
    }
}
