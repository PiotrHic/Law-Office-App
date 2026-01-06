package org.example.lawyerservice.repository;

import org.example.lawyerservice.domain.Lawyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.UUID;

public interface LawyerRepository extends MongoRepository<Lawyer, UUID> {

    List<Lawyer> findByName(String name);
}
