package org.example.lawclientservice.repository;

import org.example.lawclientservice.document.IdempotencyKeyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyKeyRepository
        extends MongoRepository<IdempotencyKeyDocument, String> {
}