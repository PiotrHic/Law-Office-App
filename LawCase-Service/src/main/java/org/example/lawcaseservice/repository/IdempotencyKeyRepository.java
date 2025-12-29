package org.example.lawcaseservice.repository;

import org.example.lawcaseservice.document.IdempotencyKeyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdempotencyKeyRepository
        extends MongoRepository<IdempotencyKeyDocument, String> {
}
