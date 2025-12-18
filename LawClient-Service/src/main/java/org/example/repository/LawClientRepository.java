package org.example.repository;

import org.example.domain.LawClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LawClientRepository extends MongoRepository<LawClient, String> {

    List<LawClient> findByName(String name);
}
