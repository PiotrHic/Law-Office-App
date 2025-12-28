package org.example.lawyerservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawyerResponseDto {

    private String id;
    private String name;
    private List<LawCaseDto> lawCases;
}
