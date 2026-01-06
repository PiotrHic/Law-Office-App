package org.example.lawcaseservice.repository;

import org.example.lawcaseservice.domain.LawCase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface LawCaseRepository extends MongoRepository<LawCase, UUID> {

    List<LawCase> findByName(String name);
}
