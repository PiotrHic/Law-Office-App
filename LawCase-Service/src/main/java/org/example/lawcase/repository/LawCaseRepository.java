package org.example.lawcase.repository;

import org.example.lawcase.domain.LawCase;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LawCaseRepository extends MongoRepository<LawCase, String> {

    List<LawCase> findByName(String name);
}
