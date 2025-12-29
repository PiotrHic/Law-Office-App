package org.example.lawcaseservice.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Document(collection = "lawcases_2026")
public class LawCase {

    public LawCase(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank(message = "Name is required!")
    @Size(min=4, message = "Name of the case must have at least 4 characters!")
    private String name;
    private String lawyerId;
    private String lawClientId;
    private Lawyer lawyer;
    private LawClient lawClient;

    public LawCase(String name, String lawyerId, String lawClientId, Lawyer lawyer, LawClient lawClient) {
        this.name = name;
        this.lawyerId = lawyerId;
        this.lawClientId = lawClientId;
        this.lawyer = lawyer;
        this.lawClient = lawClient;
    }
}
