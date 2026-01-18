package org.example.lawcaseservice.service;

import org.example.lawcaseservice.domain.LawCase;
import org.example.lawcaseservice.repository.LawCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(LawCaseServiceImpl.class)
class LawCaseServiceImplIntegrationTest {

    @Autowired
    private LawCaseRepository repository;

    @Autowired
    private LawCaseServiceImpl service;

    private LawCase case1;
    private LawCase case2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        case1 = new LawCase(UUID.randomUUID(), "Case 1");
        case2 = new LawCase(UUID.randomUUID(), "Case 2");

        repository.save(case1);
        repository.save(case2);
    }

    @Test
    void testCreateClient() {
        LawCase newCase = new LawCase(null, "Case 3");
        Optional<LawCase> created = service.createLawCase(newCase);

        assertThat(created).isPresent();
        assertThat(created.get().getId()).isNotNull();
        assertThat(created.get().getName()).isEqualTo("Case 3");

        List<LawCase> all = repository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void testGetLawClientByID() {
        Optional<LawCase> found = service.getLawCaseById(case1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Case 1");
    }

    @Test
    void testGetLawClientsByName() {
        List<LawCase> result = service.getLawCaseByName("Case 2");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(case2.getId());
    }

    @Test
    void testGetAllLawClients() {
        List<LawCase> all = service.getAllLawCases();

        assertThat(all).hasSize(2);
    }

    @Test
    void testUpdateLawClientById() {
        LawCase updatedInfo = new LawCase(null, "Case 4");

        Optional<LawCase> updated = service.updateLawCaseById(case1.getId(), updatedInfo);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Case 4");

        Optional<LawCase> fromDb = repository.findById(case1.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getName()).isEqualTo("Case 4");
    }

    @Test
    void testDeleteLawClientById() {
        Optional<LawCase> deleted = service.deleteLawCaseById(case2.getId());

        assertThat(deleted).isPresent();
        assertThat(deleted.get().getName()).isEqualTo("Case 2");

        List<LawCase> all = repository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    void testDeleteAllLawClients() {
        String result = service.deleteAllLawCases();

        assertThat(result).contains("Deleted 2 LawCases");

        List<LawCase> all = repository.findAll();
        assertThat(all).isEmpty();
    }
}