package org.example.lawyerservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Document(collection = "lawyers_2026")
public class Lawyer {

    public Lawyer(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
    @Id
    private UUID id;
    @Indexed(unique = true)
    @NotBlank(message = "Name is required!")
    @Size(min=4, message = "Name must have at least 4 characters!")
    private String name;
    private List<LawCase> lawCaseList;
}
