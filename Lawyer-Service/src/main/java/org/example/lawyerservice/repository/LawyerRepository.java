package org.example.lawyerservice.repository;

import org.example.lawyerservice.domain.Lawyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LawyerRepository extends MongoRepository<Lawyer, String> {

    List<Lawyer> findByName(String name);
}
