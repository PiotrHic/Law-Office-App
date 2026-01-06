package org.example.lawyerservice.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseDto {

    private UUID id;

    @NotBlank(message = "Law case name cannot be blank")
    @Size(min = 3, message = "Law case name must be at least 3 characters long")
    private String name;

    @NotBlank(message = "LawyerId cannot be blank")
    private String lawyerId;
}
