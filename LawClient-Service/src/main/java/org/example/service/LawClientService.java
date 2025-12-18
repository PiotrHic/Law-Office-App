package org.example.service;


import org.example.domain.LawClient;

import java.util.List;
import java.util.Optional;

public interface LawClientService {

    Optional<LawClient> createClient( LawClient lawClient);
    Optional<LawClient> getLawClientByID(String lawClientId);
    List<LawClient> getLawClientsByName(String name);
    List<LawClient> getAllLawClients();
    Optional<LawClient> updateLawClientById(String lawClientId, LawClient lawClient);
    Optional<LawClient> deleteLawClientById(String lawClientId);
    String deleteAllLawClients();
}

