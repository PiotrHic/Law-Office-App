package org.example.lawcaseservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;import org.example.lawcaseservice.domain.LawClient;import org.example.lawcaseservice.domain.Lawyer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseResponseDto {

    private String id;
    private String name;
    private String lawyerId;
    private String lawClientId;
    private Lawyer lawyer;
    private LawClient lawClient;
}
