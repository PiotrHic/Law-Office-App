package org.example.lawyerservice.domain;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCase {

    public LawCase(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    private UUID id;
    private String name;
    private String lawyerId;
    //private String lawClientId;
    // private Object lawClient;
}
