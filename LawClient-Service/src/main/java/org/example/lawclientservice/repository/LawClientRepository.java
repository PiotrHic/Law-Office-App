package org.example.lawclientservice.repository;

import org.example.lawclientservice.domain.LawClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LawClientRepository extends MongoRepository<LawClient, String> {

    List<LawClient> findByName(String name);
}
