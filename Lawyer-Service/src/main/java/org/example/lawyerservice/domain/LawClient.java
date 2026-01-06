package org.example.lawyerservice.domain;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClient {

    private UUID id;
    private String name;
}