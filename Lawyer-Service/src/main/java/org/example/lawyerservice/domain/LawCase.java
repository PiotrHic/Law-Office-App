package org.example.lawyerservice.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCase {

    public LawCase(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;
    private String lawClientId;
    private Object lawClient;
}
