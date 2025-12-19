package org.example.lawclientservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClientResponseDto {

    private String id;
    private String name;
    private List<LawCaseDto> lawCases;
}
