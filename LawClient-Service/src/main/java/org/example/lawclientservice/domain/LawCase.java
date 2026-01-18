package org.example.lawclientservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCase {

    public LawCase ( UUID id, String name, UUID lawyerId ) {
        this.id = id;
        this.name = name;
        this.lawyerId = lawyerId;
    }

    private UUID id;
    private String name;
    private UUID lawyerId;
    private Lawyer lawyer;

}
