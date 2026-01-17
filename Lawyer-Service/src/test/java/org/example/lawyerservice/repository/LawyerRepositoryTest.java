package org.example.lawyerservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.example.lawyerservice.domain.Lawyer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class LawyerRepositoryTest {

    @Autowired
    private LawyerRepository lawyerRepository;

    private Lawyer lawyer1;
    private Lawyer lawyer2;

    @BeforeEach
    void setUp() {
        // Czyścimy bazę przed każdym testem
        lawyerRepository.deleteAll();

        lawyer1 = new Lawyer(UUID.randomUUID(), "Jan Kowalski");
        lawyer2 = new Lawyer(UUID.randomUUID(), "Anna Nowak");

        lawyerRepository.save(lawyer1);
        lawyerRepository.save(lawyer2);
    }

    @Test
    void testFindByName() {
        List<Lawyer> result = lawyerRepository.findByName("Jan Kowalski");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Jan Kowalski");
    }

    @Test
    void testFindAll() {
        List<Lawyer> all = lawyerRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void testDelete() {
        lawyerRepository.delete(lawyer1);
        List<Lawyer> all = lawyerRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Anna Nowak");
    }

    @Test
    void testSave() {
        Lawyer newLawyer = new Lawyer(UUID.randomUUID(), "Piotr Zielinski");
        lawyerRepository.save(newLawyer);

        List<Lawyer> all = lawyerRepository.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).extracting("name").contains("Piotr Zielinski");
    }
}