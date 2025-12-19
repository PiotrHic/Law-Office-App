package org.example.lawclientservice.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "idempotency_keys")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyKeyDocument {

    @Id
    private String idempotencyKey;

    private String responseBody;

    private int responseStatus;

    @Indexed(expireAfterSeconds = 86400) // 24h TTL
    private Instant createdAt;
}
