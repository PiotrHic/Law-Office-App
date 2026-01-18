package org.example.lawcaseservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.example.lawcaseservice.domain.LawCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class LawCaseRepositoryTest {

    @Autowired
    private LawCaseRepository lawCaseRepository;

    private LawCase case1;
    private LawCase case2;

    @BeforeEach
    void setUp() {
        // Czyścimy kolekcję
        lawCaseRepository.deleteAll();

        case1 = new LawCase(UUID.randomUUID(), "Sprawa Jana");
        case2 = new LawCase(UUID.randomUUID(), "Sprawa Anny");

        lawCaseRepository.save(case1);
        lawCaseRepository.save(case2);
    }

    @Test
    void testFindByName() {
        List<LawCase> result = lawCaseRepository.findByName("Sprawa Jana");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Sprawa Jana");
    }

    @Test
    void testFindAll() {
        List<LawCase> all = lawCaseRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void testDelete() {
        lawCaseRepository.delete(case1);

        List<LawCase> all = lawCaseRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Sprawa Anny");
    }

    @Test
    void testSave() {
        LawCase newCase = new LawCase(UUID.randomUUID(), "Sprawa Piotra");
        lawCaseRepository.save(newCase);

        List<LawCase> all = lawCaseRepository.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).extracting("name").contains("Sprawa Piotra");
    }
}

