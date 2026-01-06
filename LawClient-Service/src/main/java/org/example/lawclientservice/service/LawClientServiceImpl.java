package org.example.lawclientservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.repository.LawClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LawClientServiceImpl implements LawClientService {

    private final LawClientRepository repository;

    // CREATE
    @Override
    @Timed(value = "lawclient.create", percentiles = {0.95, 0.99})
    public Optional<LawClient> createClient(LawClient lawClient) {
        log.info("Creating LawClient with name='{}'", lawClient.getName());
        if (lawClient.getId() == null) {
            lawClient.setId(UUID.randomUUID());
        }
        LawClient saved = repository.save(lawClient);
        log.info("LawClient created with id={}", saved.getId());
        return Optional.of(saved);
    }

    // READ by ID
    @Override
    @Timed(value = "lawclient.getById", percentiles = {0.95, 0.99})
    public Optional<LawClient> getLawClientByID(UUID lawClientId) {
        log.debug("Fetching LawClient by id={}", lawClientId);
        return repository.findById(lawClientId);
    }

    // READ by name
    @Override
    @Timed(value = "lawclient.getByName", percentiles = {0.95, 0.99})
    public List<LawClient> getLawClientsByName(String name) {
        log.debug("Fetching LawClient by name={}", name);
        return repository.findByName(name);
    }

    // READ all
    @Override
    @Timed(value = "lawclient.getAll", percentiles = {0.95, 0.99})
    public List<LawClient> getAllLawClients() {
        log.debug("Fetching all LawClients");
        return repository.findAll();
    }

    // UPDATE by ID
    @Override
    @Timed(value = "lawclient.update", percentiles = {0.95, 0.99})
    public Optional<LawClient> updateLawClientById(UUID lawClientId, LawClient lawClient) {
        log.info("Updating LawClient id={}", lawClientId);
        return repository.findById(lawClientId)
                .map(existing -> {
                    existing.setName(lawClient.getName());
                    existing.setLawCases(lawClient.getLawCases());
                    LawClient updated = repository.save(existing);
                    log.info("LawClient updated id={}", updated.getId());
                    return updated;
                });
    }

    // DELETE by ID
    @Override
    @Timed(value = "lawclient.delete", percentiles = {0.95, 0.99})
    public Optional<LawClient> deleteLawClientById(UUID lawClientId) {
        log.info("Deleting LawClient id={}", lawClientId);
        return repository.findById(lawClientId)
                .map(client -> {
                    repository.delete(client);
                    log.info("LawClient deleted id={}", lawClientId);
                    return client;
                });
    }

    @Override
    @Timed(value = "lawclient.deleteAll", percentiles = {0.95, 0.99})
    public String deleteAllLawClients() {
        long count = repository.count();
        repository.deleteAll();
        log.info("Deleted {} LawClients", count);
        return "Deleted " + count + " LawClients. Database is empty!";
    }

}
