package org.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "law_clients_2026")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClient {

    @Id
    private String id;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    private List<LawCase> lawCases;
}
