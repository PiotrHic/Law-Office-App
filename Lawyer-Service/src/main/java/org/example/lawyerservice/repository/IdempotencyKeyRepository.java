package org.example.lawyerservice.repository;

import org.example.lawyerservice.document.IdempotencyKeyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdempotencyKeyRepository
        extends MongoRepository<IdempotencyKeyDocument, String> {
}
