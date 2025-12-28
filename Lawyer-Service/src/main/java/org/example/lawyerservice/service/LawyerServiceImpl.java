package org.example.lawyerservice.service;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.repository.LawyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LawyerServiceImpl implements LawyerService {

    private final LawyerRepository repository;

    // CREATE
    @Override
    @Timed(value = "lawyer.create", percentiles = {0.95, 0.99})
    public Optional<Lawyer> createLawyer(Lawyer lawyer) {
        log.info("Creating Lawyer with name='{}'", lawyer.getName());
        Lawyer saved = repository.save(lawyer);
        log.info("Lawyer created with id={}", saved.getId());
        return Optional.of(saved);
    }

    // READ by ID
    @Override
    @Timed(value = "lawyer.getById", percentiles = {0.95, 0.99})
    public Optional<Lawyer> getLawyerByID(String lawyerId) {
        log.debug("Fetching Lawyer by id={}", lawyerId);
        return repository.findById(lawyerId);
    }

    // READ by name
    @Override
    @Timed(value = "lawyer.getByName", percentiles = {0.95, 0.99})
    public List<Lawyer> getLawyerByName(String name) {
        log.debug("Fetching Lawyer by name={}", name);
        return repository.findByName(name);
    }

    // READ all
    @Override
    @Timed(value = "lawyer.getAll", percentiles = {0.95, 0.99})
    public List<Lawyer> getAllLawyers() {
        log.debug("Fetching all Lawyers");
        return repository.findAll();
    }

    // UPDATE by ID
    @Override
    @Timed(value = "lawyer.update", percentiles = {0.95, 0.99})
    public Optional<Lawyer> updateLawyerById(String lawyerId, Lawyer lawyer) {
        log.info("Updating Lawyer id={}", lawyerId);
        return repository.findById(lawyerId)
                .map(existing -> {
                    existing.setName(lawyer.getName());
                    existing.setLawCaseList(lawyer.getLawCaseList());
                    Lawyer updated = repository.save(existing);
                    log.info("Lawyer updated id={}", updated.getId());
                    return updated;
                });
    }

    // DELETE by ID
    @Override
    @Timed(value = "lawyer.delete", percentiles = {0.95, 0.99})
    public Optional<Lawyer> deleteLawyerById(String lawyerId) {
        log.info("Deleting Lawyer id={}", lawyerId);
        return repository.findById(lawyerId)
                .map(client -> {
                    repository.delete(client);
                    log.info("LawClient deleted id={}", lawyerId);
                    return client;
                });
    }

    @Override
    @Timed(value = "lawyer.deleteAll", percentiles = {0.95, 0.99})
    public String deleteAllLawyers() {
        long count = repository.count();
        repository.deleteAll();
        log.info("Deleted {} Lawyers", count);
        return "Deleted " + count + " Lawyers. Database is empty!";
    }

}
