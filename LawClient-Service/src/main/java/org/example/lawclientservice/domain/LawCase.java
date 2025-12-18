package org.example.lawclientservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCase {

    private String id;
    private String name;
    private String lawyerId;
    private Lawyer lawyer;
}
