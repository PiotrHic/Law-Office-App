package org.example.lawyerservice.service;

import org.example.lawyerservice.domain.Lawyer;
import org.example.lawyerservice.repository.LawyerRepository;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DataMongoTest
@Import(LawyerServiceImpl.class)
public class LawyerServiceImplIntegrationTest {

    @Autowired
    private LawyerRepository repository;

    @Autowired
    private LawyerServiceImpl service;

    private Lawyer lawyer1;
    private Lawyer lawyer2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        lawyer1 = new Lawyer(UUID.randomUUID(), "Jan Kowalski");
        lawyer2 = new Lawyer(UUID.randomUUID(), "Anna Nowak");

        repository.save(lawyer1);
        repository.save(lawyer2);
    }

    @Test
    void testCreateLawyer() {
        Lawyer newLawyer = new Lawyer(null, "Piotr Zielinski");
        Optional<Lawyer> created = service.createLawyer(newLawyer);

        assertThat(created).isPresent();
        assertThat(created.get().getId()).isNotNull();
        assertThat(created.get().getName()).isEqualTo("Piotr Zielinski");

        List<Lawyer> all = repository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void testGetLawyerById() {
        Optional<Lawyer> found = service.getLawyerById(lawyer1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jan Kowalski");
    }

    @Test
    void testGetLawyerByName() {
        List<Lawyer> result = service.getLawyerByName("Anna Nowak");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Anna Nowak");
    }

    @Test
    void testGetAllLawyers() {
        List<Lawyer> all = service.getAllLawyers();
        assertThat(all).hasSize(2);
    }

    @Test
    void testUpdateLawyerById() {
        Lawyer updatedInfo = new Lawyer(null, "Jan Kowalski Updated");
        Optional<Lawyer> updated = service.updateLawyerById(lawyer1.getId(), updatedInfo);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Jan Kowalski Updated");

        Optional<Lawyer> fromDb = repository.findById(lawyer1.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getName()).isEqualTo("Jan Kowalski Updated");
    }

    @Test
    void testDeleteLawyerById() {
        Optional<Lawyer> deleted = service.deleteLawyerById(lawyer2.getId());

        assertThat(deleted).isPresent();
        assertThat(deleted.get().getName()).isEqualTo("Anna Nowak");

        List<Lawyer> all = repository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    void testDeleteAllLawyers() {
        String result = service.deleteAllLawyers();
        assertThat(result).contains("Deleted 2 Lawyers");

        List<Lawyer> all = repository.findAll();
        assertThat(all).isEmpty();
    }
}
