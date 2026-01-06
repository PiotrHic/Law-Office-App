package org.example.lawcaseservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;import org.example.lawcaseservice.domain.LawClient;import org.example.lawcaseservice.domain.Lawyer;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseResponseDto {

    private UUID id;
    private String name;
    private UUID lawyerId;
    private UUID lawClientId;
    private Lawyer lawyer;
    private LawClient lawClient;
}
