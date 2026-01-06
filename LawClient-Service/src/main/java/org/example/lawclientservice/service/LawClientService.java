package org.example.lawclientservice.service;


import org.example.lawclientservice.domain.LawClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LawClientService {

    Optional<LawClient> createClient(LawClient lawClient);
    Optional<LawClient> getLawClientByID( UUID lawClientId);
    List<LawClient> getLawClientsByName(String name);
    List<LawClient> getAllLawClients();
    Optional<LawClient> updateLawClientById(UUID lawClientId, LawClient lawClient);
    Optional<LawClient> deleteLawClientById(UUID lawClientId);
    String deleteAllLawClients();
}

