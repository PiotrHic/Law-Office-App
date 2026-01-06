package org.example.lawcaseservice.service;

import org.example.lawcaseservice.domain.LawCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LawCaseService {

    Optional<LawCase> createLawCase(LawCase lawCase);
    Optional<LawCase> getLawCaseById(UUID id);
    List<LawCase> getLawCaseByName(String name);
    List<LawCase> getAllLawCases();
    Optional<LawCase> updateLawCaseById(UUID id, LawCase lawCase);
    Optional<LawCase> deleteLawCaseById(UUID id);
    String deleteAllLawCases();
}
