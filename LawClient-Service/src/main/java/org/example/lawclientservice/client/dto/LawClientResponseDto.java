package org.example.lawclientservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClientResponseDto {

    private UUID id;
    private String name;
    private List<LawCaseDto> lawCases;
}
