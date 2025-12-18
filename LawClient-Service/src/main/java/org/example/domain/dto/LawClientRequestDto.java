package org.example.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClientRequestDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    private List<LawCaseDto> lawCases;
}