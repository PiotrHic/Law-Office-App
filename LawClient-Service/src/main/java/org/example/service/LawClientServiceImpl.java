package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.LawClient;
import org.example.repository.LawClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LawClientServiceImpl implements LawClientService {

    private final LawClientRepository repository;

    // CREATE
    @Override
    public Optional<LawClient> createClient( LawClient lawClient) {
        log.info("Creating LawClient with name='{}'", lawClient.getName());

        LawClient saved = repository.save(lawClient);

        log.info("LawClient created with id={}", saved.getId());
        return Optional.of(saved);
    }

    // READ by ID
    @Override
    public Optional<LawClient> getLawClientByID(String lawClientId) {
        log.debug("Fetching LawClient by id={}", lawClientId);

        Optional<LawClient> client = repository.findById(lawClientId);

        if (client.isEmpty()) {
            log.warn("LawClient not found, id={}", lawClientId);
        }

        return client;
    }

    @Override
    public List<LawClient> getLawClientsByName(String name) {
        log.debug("Fetching LawClient by name={}", name);

        List<LawClient> clients = repository.findByName(name);

        if (clients.isEmpty()) {
            log.warn ( "LawClient not found, name={}", name );
        }

        return clients;
    }

    // READ all
    @Override
    public List<LawClient> getAllLawClients() {
        log.debug("Fetching all LawClients");

        List<LawClient> clients = repository.findAll();

        log.info("Fetched {} LawClients", clients.size());
        return clients;
    }

    // UPDATE by ID
    @Override
    public Optional<LawClient> updateLawClientById(String lawClientId, LawClient lawClient) {
        log.info("Updating LawClient id={}", lawClientId);

        return repository.findById(lawClientId)
                .map(existing -> {
                    log.debug(
                            "Updating LawClient id={} | oldName='{}' newName='{}'",
                            lawClientId,
                            existing.getName(),
                            lawClient.getName()
                    );

                    existing.setName(lawClient.getName());
                    existing.setLawCases(lawClient.getLawCases());

                    LawClient updated = repository.save(existing);
                    log.info("LawClient updated id={}", updated.getId());

                    return updated;
                })
                .or(() -> {
                    log.warn("Cannot update LawClient, not found id={}", lawClientId);
                    return Optional.empty();
                });
    }

    // DELETE by ID
    @Override
    public Optional<LawClient> deleteLawClientById(String lawClientId) {
        log.info("Deleting LawClient id={}", lawClientId);

        return repository.findById(lawClientId)
                .map(client -> {
                    repository.delete(client);
                    log.info("LawClient deleted id={}", lawClientId);
                    return client;
                })
                .or(() -> {
                    log.warn("Cannot delete LawClient, not found id={}", lawClientId);
                    return Optional.empty();
                });
    }

    // DELETE all
    @Override
    public String deleteAllLawClients() {
        long count = repository.count();

        log.warn("Deleting ALL LawClients, count={}", count);

        repository.deleteAll();

        log.info("Deleted {} LawClients", count);
        return "Deleted " + count + " LawClients. Database is empty!";
    }
}
