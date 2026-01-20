package org.example.lawcaseservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Document(collection = "law_cases_2026")
public class LawCase {

    public LawCase(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public LawCase (UUID id, String name, UUID lawyerId) {
        this.name = name;
        this.lawyerId = lawyerId;
        this.id = id;
    }

    @Id
    private UUID id;
    @Indexed(unique = true)
    @NotBlank(message = "Name is required!")
    @Size(min=4, message = "Name of the case must have at least 4 characters!")
    private String name;
    private UUID lawyerId;
    private UUID lawClientId;
    private Lawyer lawyer;
    private LawClient lawClient;

    public LawCase(String name, UUID lawyerId, UUID lawClientId, Lawyer lawyer, LawClient lawClient) {
        this.name = name;
        this.lawyerId = lawyerId;
        this.lawClientId = lawClientId;
        this.lawyer = lawyer;
        this.lawClient = lawClient;
    }

}
