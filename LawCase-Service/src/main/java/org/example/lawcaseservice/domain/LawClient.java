package org.example.lawcaseservice.domain;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LawClient {

    private UUID id;
    private String name;

}
