package org.example.lawyerservice.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawClient {

    private String id;
    private String name;
}