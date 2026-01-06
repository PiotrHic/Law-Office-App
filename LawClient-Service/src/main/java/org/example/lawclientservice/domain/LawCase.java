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

    private UUID id;
    private String name;
    private UUID lawyerId;
    private Lawyer lawyer;
}
