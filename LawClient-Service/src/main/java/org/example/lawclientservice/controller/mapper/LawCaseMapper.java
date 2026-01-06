package org.example.lawclientservice.controller.mapper;

import org.example.lawclientservice.client.dto.LawCaseDto;
import org.example.lawclientservice.domain.LawCase;
import org.springframework.stereotype.Component;

@Component
public class LawCaseMapper {

    public static LawCaseDto toDTO(LawCase lawcase) {
        if(lawcase == null) {
            return null;
        }
        return LawCaseDto.builder()
                .id(lawcase.getId())
                .name(lawcase.getName())
                .lawyerId(lawcase.getLawyerId())
                .build();
    }

    public static LawCase toEntity(LawCaseDto dto) {
        if(dto == null) {
            return null;
        }
        return LawCase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lawyerId(dto.getLawyerId())
                .build();
    }

}
