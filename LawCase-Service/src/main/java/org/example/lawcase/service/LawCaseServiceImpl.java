package org.example.lawcase.service;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lawcase.domain.LawCase;
import org.example.lawcase.repository.LawCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class LawCaseServiceImpl implements LawCaseService {

    @Autowired
    private final LawCaseRepository repository;

    // CREATE
    @Override
    @Timed(value = "lawcase.create", percentiles = {0.95, 0.99})
    public Optional<LawCase> createLawCase(LawCase lawCase) {
        log.info("Creating LawCase with name='{}'", lawCase.getName());
        LawCase saved = repository.save(lawCase);
        log.info("Lawyer created with id={}", saved.getId());
        return Optional.of(saved);
    }

    // READ by ID
    @Override
    @Timed(value = "lawCase.getById", percentiles = {0.95, 0.99})
    public Optional<LawCase> getLawCaseById(String lawCaseId) {
        log.debug("Fetching LawCase by id={}", lawCaseId);
        return repository.findById(lawCaseId);
    }

    // READ by name
    @Override
    @Timed(value = "lawCase.getByName", percentiles = {0.95, 0.99})
    public List<LawCase> getLawCaseByName(String name) {
        log.debug("Fetching LawCase by name={}", name);
        return repository.findByName(name);
    }

    // READ all
    @Override
    @Timed(value = "lawCase.getAll", percentiles = {0.95, 0.99})
    public List<LawCase> getAllLawCases() {
        log.debug("Fetching all LawCases");
        return repository.findAll();
    }


    // UPDATE by ID
    @Override
    @Timed(value = "lawCase.update", percentiles = {0.95, 0.99})
    public Optional<LawCase> updateLawCaseById(String lawCaseId, LawCase lawCase) {
        log.info("Updating LawCase id={}", lawCaseId);
        return repository.findById(lawCaseId)
                .map(existing -> {
                    existing.setName(lawCase.getName());
                    existing.setLawClient(lawCase.getLawClient());
                    existing.setLawClientId (lawCase.getLawClientId());
                    existing.setLawyer(lawCase.getLawyer());
                    existing.setLawyerId(lawCase.getLawyerId());
                    LawCase updated = repository.save(existing);
                    log.info("LawCase updated id={}", updated.getId());
                    return updated;
                });
    }

    // DELETE by ID
    @Override
    @Timed(value = "lawcase.delete", percentiles = {0.95, 0.99})
    public Optional<LawCase> deleteLawCaseById(String lawCaseId) {
        log.info("Deleting LawCase id={}", lawCaseId);
        return repository.findById(lawCaseId)
                .map(client -> {
                    repository.delete(client);
                    log.info("LawCase deleted id={}", lawCaseId);
                    return client;
                });
    }

    @Override
    @Timed(value = "lawCases.deleteAll", percentiles = {0.95, 0.99})
    public String deleteAllLawCases() {
        long count = repository.count();
        repository.deleteAll();
        log.info("Deleted {} LawCases", count);
        return "Deleted " + count + " LawCases. Database is empty!";
    }
}
