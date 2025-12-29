package org.example.lawcaseservice.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lawcaseservice.domain.LawClient;
import org.example.lawcaseservice.domain.Lawyer;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseRequestDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    private String lawyerId;
    private String lawClientId;
    private Lawyer lawyer;
    private LawClient lawClient;
}