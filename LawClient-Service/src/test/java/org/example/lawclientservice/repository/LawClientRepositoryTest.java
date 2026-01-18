package org.example.lawclientservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.lawclientservice.domain.LawClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.UUID;

@DataMongoTest
class LawClientRepositoryTest {

    @Autowired
    private LawClientRepository lawClientRepository;

    private LawClient lawClient1;
    private LawClient lawClient2;

    @BeforeEach
    void setUp() {
        // Czyścimy bazę przed każdym testem
        lawClientRepository.deleteAll();

        lawClient1 = LawClient.builder().id(UUID.randomUUID()).name("Jan Kowalski").build();
        lawClient2 = LawClient.builder().id(UUID.randomUUID()).name("Anna Nowak").build();

        lawClientRepository.save(lawClient1);
        lawClientRepository.save(lawClient2);
    }

    @Test
    void testFindByName() {
        List<LawClient> result = lawClientRepository.findByName("Jan Kowalski");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Jan Kowalski");
    }

    @Test
    void testFindAll() {
        List<LawClient> all = lawClientRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void testDelete() {
        lawClientRepository.delete(lawClient1);
        List<LawClient> all = lawClientRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Anna Nowak");
    }

    @Test
    void testSave() {
        LawClient newLawyer = LawClient.builder().id(UUID.randomUUID()).name("Piotr Zielinski").build();
        lawClientRepository.save(newLawyer);

        List<LawClient> all = lawClientRepository.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).extracting("name").contains("Piotr Zielinski");
    }
}