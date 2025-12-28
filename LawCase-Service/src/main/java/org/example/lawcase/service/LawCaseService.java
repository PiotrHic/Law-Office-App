package org.example.lawcase.service;

import org.example.lawcase.domain.LawCase;

import java.util.List;
import java.util.Optional;

public interface LawCaseService {

    Optional<LawCase> createLawCase(LawCase lawCase);
    Optional<LawCase> getLawCaseById (String id);
    List<LawCase> getLawCaseByName (String name);
    List<LawCase> getAllLawCases();
    Optional<LawCase> updateLawCaseById (String id, LawCase lawCase);
    Optional<LawCase> deleteLawCaseById (String id);
    String deleteAllLawCases();
}
