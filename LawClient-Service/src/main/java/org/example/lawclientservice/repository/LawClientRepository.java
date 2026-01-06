package org.example.lawclientservice.repository;

import org.example.lawclientservice.domain.LawClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.UUID;

public interface LawClientRepository extends MongoRepository<LawClient, UUID> {

    List<LawClient> findByName(String name);
}
