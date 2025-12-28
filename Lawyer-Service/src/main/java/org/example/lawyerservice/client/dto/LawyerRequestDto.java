package org.example.lawyerservice.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawyerRequestDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    private List<LawCaseDto> lawCases;
}