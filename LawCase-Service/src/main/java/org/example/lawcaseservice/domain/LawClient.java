package org.example.lawcaseservice.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LawClient {

    private String id;
    private String name;

}
